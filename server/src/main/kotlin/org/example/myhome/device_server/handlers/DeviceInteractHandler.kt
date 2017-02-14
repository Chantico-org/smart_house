package org.example.myhome.device_server.handlers

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.concurrent.Promise

class DeviceInteractHandler : ChannelInboundHandlerAdapter() {
  var channelHandlerContext: ChannelHandlerContext? = null
  var promiseQueue:List<Promise<String>> = emptyList()

  override fun channelRegistered(ctx: ChannelHandlerContext?) {
    channelHandlerContext = ctx
    super.channelRegistered(ctx)
  }

  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    println(msg)
    when(msg) {
      is String -> {
        val promise = promiseQueue.firstOrNull()
        if (promise == null) {
          super.channelRead(ctx, msg)
          return
        }
        promise.setSuccess(msg)
        promiseQueue.drop(1)
      }
      else -> {
        super.channelRead(ctx, msg)
      }
    }
  }

  fun readMessage(): Promise<String> {
//    TODO create custom Exception
    val promise = channelHandlerContext?.executor()?.newPromise<String>()
      ?: throw Exception("channel is not registered yet")
    promiseQueue += promise
    return promise
  }

  fun writeMessage(data: String): ChannelFuture? {
    return channelHandlerContext?.channel()?.writeAndFlush(data)
  }
}
