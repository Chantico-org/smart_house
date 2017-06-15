package org.example.myhome

import com.google.common.eventbus.EventBus
import io.netty.channel.nio.NioEventLoopGroup
import org.example.myhome.device_server.DeviceServer
import org.example.myhome.utils.syncChannel
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@ComponentScan
open class ApplicationContext {
  @Bean("eventBus")
  open fun eventBus():EventBus = EventBus()

  @Bean("bossGroup")
  open fun bossGroup(): NioEventLoopGroup = NioEventLoopGroup()

  @Bean("workerGroup")
  open fun workerGroup(): NioEventLoopGroup = NioEventLoopGroup()
}

fun main(args: Array<String>) {
  SpringApplication.run(ApplicationContext::class.java)
}
