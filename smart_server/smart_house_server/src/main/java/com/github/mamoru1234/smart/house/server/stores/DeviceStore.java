package com.github.mamoru1234.smart.house.server.stores;

import com.github.mamoru1234.smart.house.server.config.DeviceChannel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Component
public class DeviceStore {
  private final Map<String, DeviceChannel> cache;
  private final EventBus eventBus;
  @Autowired
  public DeviceStore(EventBus eventBus) {
    this.eventBus = eventBus;
    eventBus.register(this);
    this.cache = new HashMap<>();
  }

  @Subscribe
  public void registerDevice(DeviceChannel deviceChannel) {
    cache.put(deviceChannel.getDeviceID(), deviceChannel);
    System.out.println("Registering device by ID: " + deviceChannel.getDeviceID());
  }

  @PreDestroy
  public void destroy() {
    eventBus.unregister(this);
  }
}
