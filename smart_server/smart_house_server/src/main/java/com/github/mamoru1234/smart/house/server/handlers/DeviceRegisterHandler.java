package com.github.mamoru1234.smart.house.server.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mamoru1234.smart.house.server.config.DeviceChannel;
import com.google.common.eventbus.EventBus;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class DeviceRegisterHandler  extends SimpleChannelInboundHandler<String> {
  private static final ObjectMapper mapper = new ObjectMapper();
  private final EventBus eventBus;
  public DeviceRegisterHandler(EventBus eventBus){
    this.eventBus = eventBus;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
    DeviceChannel deviceChannel = mapper.readValue(msg, DeviceChannel.class);
    deviceChannel.setChannel(ctx.channel());
    eventBus.post(deviceChannel);
    ctx.pipeline().remove(this);
  }
}
