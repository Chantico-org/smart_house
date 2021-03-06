package org.example.myhome.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel

fun startNettyServer(
  port: Int,
  bossGroup: EventLoopGroup,
  workerGroup: EventLoopGroup,
  initializer: ChannelInitializer<SocketChannel>
): ChannelFuture? {
  val bootstrap = ServerBootstrap()
  bootstrap.group(bossGroup, workerGroup)
    .channel(NioServerSocketChannel::class.java)
    .childHandler(initializer)
    .option(ChannelOption.SO_BACKLOG, 128)
    .childOption(ChannelOption.SO_KEEPALIVE, true)
  return bootstrap.bind(port)
}
