package org.example.myhome.device_server.handlers

import com.google.common.eventbus.EventBus
import com.google.gson.Gson
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.models.Device
import org.example.myhome.models.DeviceMetaData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject

class DeviceRegistration(
  val eventBus: EventBus
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
        eventBus.post(Device(channel = ctx.channel(), deviceMetaData = deviceMetaData))
      }
    }
  }

  override fun channelInactive(ctx: ChannelHandlerContext?) {
    super.channelInactive(ctx)
  }
}
