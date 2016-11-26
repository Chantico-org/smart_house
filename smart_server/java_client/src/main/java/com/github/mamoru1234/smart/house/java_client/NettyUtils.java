package com.github.mamoru1234.smart.house.java_client;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.concurrent.Promise;

/**
 */
public class NettyUtils {
  public static Promise<Void> convertToPromise(ChannelFuture channelFuture) {
    Promise<Void> result = channelFuture.channel().eventLoop().newPromise();
    channelFuture.addListener((ChannelFutureListener) future -> {
      if (future.isSuccess()) {
        result.setSuccess(null);
      } else {
        result.setFailure(future.cause());
      }
    });
    return result;
  }
}
