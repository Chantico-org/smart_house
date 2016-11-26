package com.github.mamoru1234.smart.house.java_client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

/**
 */
public class NettyClient {
  private EventLoopGroup workerGroup = new NioEventLoopGroup();
  private Channel channel;
  private final ChannelInitializer<SocketChannel> initializer;
  public NettyClient(ChannelInitializer<SocketChannel> initializer) {
    this.initializer = initializer;
  }
  public Promise<Void> connect(String host, int port) {
    Bootstrap b = new Bootstrap()
      .group(workerGroup)
      .channel(NioSocketChannel.class)
      .option(ChannelOption.SO_KEEPALIVE, true)
      .handler(this.initializer);
    ChannelFuture connectFuture = b.connect(host, port);
    channel = connectFuture.channel();
    return NettyUtils.convertToPromise(connectFuture);
  }

  public Promise<Void> write(Object message) {
    return NettyUtils.convertToPromise(channel.write(message));
  }

  public void flush() {
    channel.flush();
  }

  public ByteBuf alloc() {
    return channel.alloc().buffer();
  }

  public Promise<Void> close() {
    return NettyUtils.convertToPromise(channel.close());
  }

  public Future<?> dispose() {
    System.out.println("dispose");
    return workerGroup.shutdownGracefully();
  }
}
