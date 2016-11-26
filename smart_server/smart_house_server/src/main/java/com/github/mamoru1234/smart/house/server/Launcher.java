package com.github.mamoru1234.smart.house.server;

import com.github.mamoru1234.smart.house.server.config.DeviceServer;
import com.google.common.eventbus.EventBus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
@ComponentScan
public class Launcher {
  @Bean
  public EventBus eventBus() {
    return new EventBus();
  }
  public static void main(String[] args) throws InterruptedException {
    ApplicationContext applicationContext =
      new AnnotationConfigApplicationContext(Launcher.class);
    DeviceServer deviceServer = applicationContext.getBean(DeviceServer.class);
    deviceServer.bind(8080).sync();
    System.out.println("Connected");
  }
}
