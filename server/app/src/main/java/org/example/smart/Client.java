package org.example.smart;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.example.smart.converters.SensorValueConverter.SensorValueEncoder;
import org.example.smart.handlers.ClientHandler;

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

    b.connect(new InetSocketAddress(8080));
  }
}
