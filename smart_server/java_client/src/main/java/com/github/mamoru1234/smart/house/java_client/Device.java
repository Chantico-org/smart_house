package com.github.mamoru1234.smart.house.java_client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 */
public class Device extends NettyClient {
  public Device() {
    super(new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
      }
    });
  }

}
