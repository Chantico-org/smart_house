package com.github.mamoru1234.smart.house.server.config;

import com.github.mamoru1234.smart.house.server.handlers.DeviceRegisterHandler;
import com.google.common.eventbus.EventBus;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 */
@Component
public class DeviceServer {
  EventLoopGroup bossGroup = new NioEventLoopGroup(1);
  EventLoopGroup workerGroup = new NioEventLoopGroup();
  ServerBootstrap b;
  @Autowired
  EventBus eventBus;

  public DeviceServer() {
    // Configure the server.
    b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
      .channel(NioServerSocketChannel.class)
      .option(ChannelOption.SO_BACKLOG, 100)
      .option(ChannelOption.SO_KEEPALIVE, true)
      .childHandler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {
          ChannelPipeline p = ch.pipeline();
          p.addLast(new JsonObjectDecoder());
          p.addLast(new StringDecoder(CharsetUtil.UTF_8));
          p.addLast(new DeviceRegisterHandler(eventBus));
          ch.closeFuture().addListener((ChannelFutureListener) future -> {
            System.out.println("Closed");
          });
        }
      });
  }
  public ChannelFuture bind(int port) throws InterruptedException {
    return b.bind(port);
  }
  @PreDestroy
  public void destroy() {
    bossGroup.shutdownGracefully();
    workerGroup.shutdownGracefully();
  }
}
