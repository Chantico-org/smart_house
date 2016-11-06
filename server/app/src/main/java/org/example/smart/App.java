package org.example.smart;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.example.smart.converters.SensorValueConverter.SensorValueEncoder;
import org.example.smart.handlers.EchoHandler;

import static org.example.smart.converters.SensorValueConverter.SensorValueDecoder;

/**
 */
public class App {
  public static void main(String... args) throws InterruptedException {
    EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      ServerBootstrap b = new ServerBootstrap(); // (2)
      b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class) // (3)
        .childHandler(new ChannelInitializer<SocketChannel>() { // (4)
          @Override
          public void initChannel(SocketChannel ch) throws Exception {
            System.out.println("init channel");
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new SensorValueEncoder());
            pipeline.addLast(new SensorValueDecoder());
            pipeline.addLast(new EchoHandler());
          }
        })
        .option(ChannelOption.SO_BACKLOG, 128)          // (5)
        .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

      // Bind and start to accept incoming connections.
      ChannelFuture f = b.bind(8080).sync(); // (7)
      // Wait until the server socket is closed.
      // In this example, this does not happen, but you can do that to gracefully
      // shut down your server.
      f.channel().closeFuture().sync();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }
}
