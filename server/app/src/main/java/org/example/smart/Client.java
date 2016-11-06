package org.example.smart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.smart.converters.SensorValueConverter.SensorValueEncoder;
import org.example.smart.handlers.ClientHandler;
import org.example.smart.models.SensorValue;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 */
public class Client {
  public static void main(String... args) throws IOException, InterruptedException {
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    Bootstrap b = new Bootstrap();
    b.group(workerGroup);
    b.channel(NioSocketChannel.class);

    b.handler(new ChannelInitializer<SocketChannel>() {
      @Override
      public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SensorValueEncoder());
        pipeline.addLast(new ClientHandler());
      }
    });

    ChannelFuture f = b.connect(new InetSocketAddress(8080)).sync();
    SensorValue sensorValue = SensorValue.builder()
      .device("fewfwf")
      .value("ewfew")
      .build();
    f.channel().writeAndFlush(sensorValue).addListener((future) -> {
      System.out.println(f.channel().isWritable());
      if (f.channel().isWritable()) {
        f.channel().writeAndFlush(sensorValue).addListener((inner) -> {
          f.channel().close();
        });
      }
    });
//    System.out.println(f.channel().isWritable());

    f.channel().closeFuture().sync();
    workerGroup.shutdownGracefully();
  }
}
