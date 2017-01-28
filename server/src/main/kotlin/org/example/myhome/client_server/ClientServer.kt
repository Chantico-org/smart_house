package org.example.myhome.client_server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpRequestEncoder
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.logging.LoggingHandler
import org.example.myhome.client_server.handlers.ClientRequestHandler

fun createClientServerInitializer(): ChannelInitializer<SocketChannel> {
  return object : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel?) {
      ch?.pipeline()?.addLast(
        LoggingHandler(),
        HttpRequestEncoder(),
        HttpRequestDecoder(),
        StringDecoder(),
        LoggingHandler(),
        ClientRequestHandler()
      )
    }
  }
}
