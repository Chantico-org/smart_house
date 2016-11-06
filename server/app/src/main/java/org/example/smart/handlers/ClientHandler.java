package org.example.smart.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.example.smart.models.SensorValue;

/**
 */
public class ClientHandler extends ChannelInboundHandlerAdapter{
  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    System.out.println("Channel active");
    SensorValue sensorValue = SensorValue.builder()
      .device("My device")
      .value("another value")
      .build();
    ctx.writeAndFlush(sensorValue);
    ctx.close();
  }
}
