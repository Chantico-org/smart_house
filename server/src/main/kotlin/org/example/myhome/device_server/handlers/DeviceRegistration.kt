package org.example.myhome.device_server.handlers

import com.google.gson.Gson
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.device_server.DTO.DeviceRegistrationResponse
import org.example.myhome.models.DeviceMetaData
import org.example.myhome.services.DeviceRegisterService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DeviceRegistration(
  val deviceRegisterService: DeviceRegisterService
) : ChannelInboundHandlerAdapter() {
  val gson = Gson()
  val logger: Logger = LoggerFactory.getLogger(javaClass)

  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (ctx == null) {
      return
    }
    when(msg) {
      is String -> {
        val deviceMetaData = gson.fromJson(msg, DeviceMetaData::class.java)
        println("Device Meta data: $deviceMetaData")
        val handler = DeviceInteractHandler()
        val registered = deviceRegisterService.registerDevice(deviceMetaData, handler)
        ctx.pipeline().remove(this)
        if (registered) {
          ctx.pipeline().addLast(handler)
          ctx.fireChannelRegistered()
          ctx.writeAndFlush(gson.toJson(DeviceRegistrationResponse(0)))
          return
        }
        ctx.writeAndFlush(gson.toJson(DeviceRegistrationResponse(1)))
      }
    }
  }

  override fun channelInactive(ctx: ChannelHandlerContext?) {
    super.channelInactive(ctx)
  }
}