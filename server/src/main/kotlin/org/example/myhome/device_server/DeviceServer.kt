package org.example.myhome.device_server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.logging.LoggingHandler


class DeviceServer(
  val port: Int
) {
  val bossGroup = NioEventLoopGroup()
  val workerGroup = NioEventLoopGroup()

  fun start(): ChannelFuture? {
    val b = ServerBootstrap()
    b.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel::class.java)
      .childHandler(object: ChannelInitializer<SocketChannel>(){
        override fun initChannel(ch: SocketChannel?) {
          ch?.pipeline()?.addLast(
            StringDecoder(),
            LoggingHandler()
          )
        }
      })
      .option(ChannelOption.SO_BACKLOG, 128)
      .childOption(ChannelOption.SO_KEEPALIVE, true)

    return b.bind(port)
  }
}
