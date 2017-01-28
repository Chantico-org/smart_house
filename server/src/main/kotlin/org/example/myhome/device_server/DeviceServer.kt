package org.example.myhome.device_server

import com.google.common.eventbus.EventBus
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.LengthFieldBasedFrameDecoder
import io.netty.handler.codec.string.StringDecoder
import org.example.myhome.device_server.handlers.DeviceRegistration


fun createDeviceServerInitializer(
  eventBus: EventBus
): ChannelInitializer<SocketChannel> {
  return object: ChannelInitializer<SocketChannel>(){
    override fun initChannel(ch: SocketChannel?) {
      ch?.pipeline()?.addLast(
        LengthFieldBasedFrameDecoder(256, 0, 1, 0, 1),
        StringDecoder(),
        DeviceRegistration(eventBus)
      )
    }
  }
}
