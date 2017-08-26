package org.example.myhome.device_server.handlers

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.models.DeviceMetaData
import org.example.myhome.services.DeviceRegisterService
import org.example.myhome.utils.objectMapper

class DeviceRegistration(
  private val deviceRegisterService: DeviceRegisterService
): ChannelInboundHandlerAdapter() {
  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (ctx == null) {
      return
    }
    if (msg is SimpMessage && msg.type == SimpMessageType.REQUEST) {
      val deviceMetaData = objectMapper.readValue(msg.body, DeviceMetaData::class.java)
      if (!deviceRegisterService.checkDevice(deviceMetaData)) {
        ctx.writeAndFlush(SimpMessage(type = SimpMessageType.RESPONSE, body = "NO"))
          .addListener(ChannelFutureListener.CLOSE)
        return
      }
      val handler = DeviceInteractHandler()
      ctx.pipeline().addLast(handler)
      ctx.fireChannelRegistered()
      deviceRegisterService.registerDevice(deviceMetaData, handler)
      ctx.pipeline().remove(this)
      return
    }
    ctx.close()
  }
}
