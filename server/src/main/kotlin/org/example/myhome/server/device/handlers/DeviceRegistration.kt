package org.example.myhome.server.device.handlers

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import org.example.myhome.dto.DeviceMetaDataDto
import org.example.myhome.server.device.LENGTH_DECODER
import org.example.myhome.server.device.createLengthDecoder
import org.example.myhome.services.DeviceRegisterService
import org.example.myhome.simp.core.SimpMessage
import org.example.myhome.simp.core.SimpMessageHandler
import org.example.myhome.simp.core.SimpMessageType
import org.example.myhome.utils.readValue

class DeviceRegistration(
  private val deviceRegisterService: DeviceRegisterService
): SimpMessageHandler() {
  override fun handleSimpMessage(ctx: ChannelHandlerContext, message: SimpMessage) {
    if (message.type != SimpMessageType.REQUEST) {
      ctx.channel().close()
      return
    }

    val deviceMetaData: DeviceMetaDataDto = readValue(message.body)

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
    ctx.pipeline()
      .replace(LENGTH_DECODER, LENGTH_DECODER, createLengthDecoder(Int.MAX_VALUE))
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    ctx.channel().close()
  }
}
