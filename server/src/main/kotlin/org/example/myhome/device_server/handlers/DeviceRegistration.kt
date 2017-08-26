package org.example.myhome.device_server.handlers

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageHandler
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.dto.DeviceMetaDataDto
import org.example.myhome.services.DeviceRegisterService
import org.example.myhome.utils.objectMapper

class DeviceRegistration(
  private val deviceRegisterService: DeviceRegisterService
): SimpMessageHandler() {
  override fun handleSimpMessage(ctx: ChannelHandlerContext, message: SimpMessage) {
    if (message.type != SimpMessageType.REQUEST) {
      ctx.channel().close()
      return
    }

    val deviceMetaData = objectMapper.readValue(message.body, DeviceMetaDataDto::class.java)

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
  }
}
