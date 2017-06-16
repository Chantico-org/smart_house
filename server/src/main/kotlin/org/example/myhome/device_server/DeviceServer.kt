package org.example.myhome.device_server

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.LengthFieldPrepender
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LoggingHandler
import org.example.myhome.device_server.handlers.DeviceRegistration
import org.example.myhome.device_server.simp.SimpCodec
import org.example.myhome.server.startNettyServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component


@Component
open class DeviceServer (
  @Qualifier("bossGroup")
  @Autowired
  val bossGroup:NioEventLoopGroup,

  @Qualifier("workerGroup")
  @Autowired
  val workerGroup:NioEventLoopGroup
) {
  open fun start(): ChannelFuture? {
    val init =object: ChannelInitializer<SocketChannel>(){
      override fun initChannel(ch: SocketChannel?) {
        ch?.pipeline()?.addLast(
          LoggingHandler(),
//          decoders
          LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 0, 4, 0, 4),
          StringDecoder(),
//          encoders
          LengthFieldPrepender(4),
          StringEncoder(),
          SimpCodec(),
          DeviceRegistration()
        )
      }
    }
    return startNettyServer(7080, bossGroup, workerGroup, init)
  }
}
