package org.example.myhome.device_server

import com.google.common.eventbus.EventBus
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.string.StringDecoder
import org.example.myhome.device_server.handlers.DeviceRegistration
import org.example.myhome.server.startNettyServer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject


@Component
open class DeviceServer (

  @Inject
  @Qualifier("bossGroup")
  val bossGroup:NioEventLoopGroup,

  @Inject
  @Qualifier("workerGroup")
  val workerGroup:NioEventLoopGroup,

  val eventBus: EventBus
) {
  open fun start(): ChannelFuture? {
    val init =object: ChannelInitializer<SocketChannel>(){
      override fun initChannel(ch: SocketChannel?) {
        ch?.pipeline()?.addLast(
          LengthFieldBasedFrameDecoder(Int.MAX_VALUE, 0, 4, 0, 4),
          StringDecoder(),
          DeviceRegistration(eventBus)
        )
      }
    }
    return startNettyServer(8080, bossGroup, workerGroup, init)
  }
}
