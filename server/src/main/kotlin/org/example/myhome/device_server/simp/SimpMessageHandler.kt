package org.example.myhome.device_server.simp

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter

/**
 * Created by Alexei Gontar.
 */
abstract class SimpMessageHandler: ChannelInboundHandlerAdapter() {
  abstract fun handleSimpMessage(ctx: ChannelHandlerContext, message: SimpMessage)

  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (ctx == null || msg == null) {
      return
    }
    when (msg) {
      is SimpMessage -> handleSimpMessage(ctx, msg)
      else -> ctx.fireChannelRead(msg)
    }
  }
}
