package com.github.mamoru1234.smart.house.server.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 */
public class ClientRegisterHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    ByteBuf in = (ByteBuf) msg;
    System.out.println(in);
//    ByteBuf sizeBuff = in.readBytes(4);
//    int size = sizeBuff.getInt(0);
//    int size1 = sizeBuff.getInt(0);
//    ReferenceCountUtil.release(sizeBuff);
//    System.out.println(size);
//    System.out.println(size1);
    super.channelRead(ctx, msg);
  }
}
