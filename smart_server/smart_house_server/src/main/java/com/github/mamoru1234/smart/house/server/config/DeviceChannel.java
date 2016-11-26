package com.github.mamoru1234.smart.house.server.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.netty.channel.Channel;
import lombok.Data;

/**
 */
@Data
public class DeviceChannel {
  @JsonIgnore
  private Channel channel;
  private String deviceID;
}
