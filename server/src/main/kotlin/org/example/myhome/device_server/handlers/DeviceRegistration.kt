package org.example.myhome.device_server.handlers

import com.google.common.eventbus.EventBus
import com.google.gson.Gson
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.models.DeviceMetaData

class DeviceRegistration(val eventBus: EventBus) : ChannelInboundHandlerAdapter() {
  val gson = Gson()
  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    when(msg) {
      is String -> {
        println("Received: $msg")
        val deviceMetaData = gson.fromJson(msg, DeviceMetaData::class.java)
        println(deviceMetaData)
        eventBus.post(deviceMetaData to ctx?.channel())
      }
      else -> {
        ctx?.fireChannelRead(msg)
      }
    }
  }
}
