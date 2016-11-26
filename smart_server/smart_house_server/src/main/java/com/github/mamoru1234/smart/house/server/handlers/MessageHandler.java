package com.github.mamoru1234.smart.house.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mamoru1234.smart.house.java_client.Model;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 */
public class MessageHandler extends SimpleChannelInboundHandler<String> {
  private ObjectMapper mapper = new ObjectMapper();
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    System.out.println(mapper.readValue(msg, Model.class));
  }
}
