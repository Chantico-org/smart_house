package org.example.myhome.device_server.handlers

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType

class DeviceRegistration: ChannelInboundHandlerAdapter() {

  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (ctx == null) {
      return
    }
    if (msg is SimpMessage) {
      println("Simp")
      println(msg)
    }
    if (msg is SimpMessage && msg.type == SimpMessageType.REQUEST) {
      ctx.writeAndFlush(SimpMessage(type = SimpMessageType.RESPONSE, body = "OK"))
        .addListener(ChannelFutureListener.CLOSE)
    }
    ctx.close()
  }
}
