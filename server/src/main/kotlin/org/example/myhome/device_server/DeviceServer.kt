package org.example.myhome.device_server

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.logging.LoggingHandler
import org.example.myhome.device_server.handlers.DeviceRegistration
import org.example.myhome.server.startNettyServer
import org.example.myhome.services.DeviceRegisterService
import org.example.myhome.simp.core.SimpCodec
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component


@Component
open class DeviceServer (
  @Qualifier("bossGroup")
  private val bossGroup:NioEventLoopGroup,

  @Qualifier("workerGroup")
  private val workerGroup:NioEventLoopGroup,

  private val deviceRegisterService: DeviceRegisterService
) {

//  sharable channel handlers
  private val lengthFieldPrepender = LengthFieldPrepender(4)
  private val simpCodec = SimpCodec()
  private val loggingHandler = LoggingHandler()

  open fun start(): ChannelFuture? {
    val init = object: ChannelInitializer<SocketChannel>(){
      override fun initChannel(ch: SocketChannel?) {
        ch?.pipeline()?.addFirst("lengthDecoder", LengthFieldBasedFrameDecoder(256, 0, 4, 0, 4))
        ch?.pipeline()?.addLast(
          lengthFieldPrepender,
          simpCodec,
          loggingHandler,
          DeviceRegistration(deviceRegisterService)
        )
      }
    }
    return startNettyServer(7080, bossGroup, workerGroup, init)
  }
}
