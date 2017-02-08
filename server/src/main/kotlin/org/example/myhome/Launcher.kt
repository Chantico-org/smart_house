package org.example.myhome

import com.google.common.eventbus.EventBus
import io.netty.channel.nio.NioEventLoopGroup
import org.example.myhome.device_server.DeviceServer
import org.example.myhome.utils.syncChannel
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class ApplicationContext {
  @Bean("eventBus")
  open fun eventBus():EventBus = EventBus()

  @Bean("bossGroup")
  open fun bossGroup():NioEventLoopGroup = NioEventLoopGroup()

  @Bean("workerGroup")
  open fun workerGroup(): NioEventLoopGroup = NioEventLoopGroup()
}

fun main(args: Array<String>) {
  val context = AnnotationConfigApplicationContext(ApplicationContext::class.java)
  val deviceServer = context.getBean(DeviceServer::class.java)
  deviceServer.start()?.syncChannel()?.closeFuture()?.sync()
  println("Stoped")
}
