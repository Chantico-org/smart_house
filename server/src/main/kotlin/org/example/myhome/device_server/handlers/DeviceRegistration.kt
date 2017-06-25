package org.example.myhome.device_server.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.extension.logger
import org.example.myhome.models.DeviceMetaData
import java.util.concurrent.TimeUnit

class DeviceRegistration: ChannelInboundHandlerAdapter() {
  companion object {
    val log by logger()
    val objectMapper = ObjectMapper().registerKotlinModule()
  }
  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (ctx == null) {
      return
    }
    if (msg is SimpMessage && msg.type == SimpMessageType.REQUEST) {
      val handler = DeviceInteractHandler()
      ctx.pipeline().addLast(handler)
      ctx.fireChannelRegistered()
      ctx.pipeline().remove(this)
      val config = objectMapper.readValue(msg.body, DeviceMetaData::class.java)
      log.debug("Config: $config")
      ctx.channel().eventLoop().schedule({
        ctx.writeAndFlush(SimpMessage(type = SimpMessageType.RESPONSE, body = "OK"))
        handler.send("control/1", "")
          .doOnNext {
            log.debug(it)
          }
          .subscribe()
      }, 1, TimeUnit.SECONDS)
      return
    }
    ctx.close()
  }
}
