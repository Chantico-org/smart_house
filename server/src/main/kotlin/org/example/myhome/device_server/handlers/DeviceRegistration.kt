package org.example.myhome.device_server.handlers

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.extension.logger
import java.util.concurrent.TimeUnit

class DeviceRegistration: ChannelInboundHandlerAdapter() {
  companion object {
    val log by logger()
  }
  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (ctx == null) {
      return
    }
    if (msg is SimpMessage && msg.type == SimpMessageType.REQUEST) {
      val handler = DeviceInteractHandler()
      ctx.channel().eventLoop().schedule({
        log.debug("Subscribing on /temp")
        handler.send("/temp", "my Body")
          .doOnNext { log.debug("Message: $it") }
          .subscribe()
      }, 1, TimeUnit.SECONDS)
      ctx.pipeline().addLast(handler)
      ctx.fireChannelRegistered()
      ctx.pipeline().remove(this)
      ctx.writeAndFlush(SimpMessage(type = SimpMessageType.RESPONSE, body = "OK"))
      return
    }
    ctx.close()
  }
}
