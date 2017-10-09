package org.example.myhome.server.device.handlers

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import org.example.myhome.dto.DeviceMetaDataDto
import org.example.myhome.services.DeviceRegisterService
import org.example.myhome.simp.core.SimpMessage
import org.example.myhome.simp.core.SimpMessageHandler
import org.example.myhome.simp.core.SimpMessageType
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
    val lengthFieldDecoder = LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 0, 4, 0, 4)
    ctx.pipeline().replace("lengthDecoder", "lengthDecoder", lengthFieldDecoder)
  }

  override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
    ctx.channel().close()
  }
}
