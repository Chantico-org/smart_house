package org.example.myhome.device_server.handlers

import com.google.gson.Gson
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
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
        logger.debug("Device Meta data: $deviceMetaData")
        val handler = DeviceInteractHandler()
        ctx.pipeline().addLast(handler)
        ctx.pipeline().remove(this)
        ctx.pipeline().addLast(DeviceInteractHandler())
        ctx.fireChannelRegistered()
        handler.readMessage().addListener { pr ->
          println("Received ${pr.get()}")
        }
      }
    }
  }

  override fun channelInactive(ctx: ChannelHandlerContext?) {
    super.channelInactive(ctx)
  }
}
