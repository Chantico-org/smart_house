package org.example.myhome.client_server.handlers

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener.CLOSE
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.handler.codec.http.DefaultFullHttpResponse
import io.netty.handler.codec.http.HttpContent
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpUtil
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.netty.handler.codec.http.HttpVersion.HTTP_1_1
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class ClientRequestHandler : ChannelInboundHandlerAdapter(){
  val logger: Logger = LoggerFactory.getLogger(javaClass)

  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    when(msg) {
      is HttpRequest -> {
        logger.debug("URI: ${msg.uri()}")
        logger.debug("METHOD: ${msg.method()}")
      }
      is HttpContent -> {
//        println("Message : ${msg.}")
        println(msg)
        val data = "Hello world"
        val response = DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(data.toByteArray()))
        response.headers().set(CONTENT_TYPE, "text/plain")
        HttpUtil.setContentLength(response, data.length.toLong())
        ctx?.writeAndFlush(response)?.addListener(CLOSE)
      }
      else -> {
        super.channelRead(ctx, msg)
      }
    }
  }
}
