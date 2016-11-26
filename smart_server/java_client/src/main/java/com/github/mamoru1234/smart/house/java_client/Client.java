package com.github.mamoru1234.smart.house.java_client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 */
public class Client extends NettyClient{

  public Client() {
    super(new ChannelInitializer<SocketChannel>() {
      @Override
      protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
      }
    });
  }
}
