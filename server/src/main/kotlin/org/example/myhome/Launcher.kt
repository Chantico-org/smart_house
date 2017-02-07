package org.example.myhome

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.example.myhome.services.DeviceRegister
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class ApplicationContext {
  @Bean("eventBus")
  open fun eventBus():EventBus = EventBus()
  @Bean("eventBus1")
  open fun eventBus1():EventBus = EventBus()
//  @Bean(name = "")
}

class Test(val eventBus: EventBus) {
  init {
    eventBus.register(this)
  }
  @Subscribe
  fun catchString(value:String) {
    println(value)
  }
}

fun main(args: Array<String>) {
//  val bossGroup = NioEventLoopGroup()
//  val workerGroup = NioEventLoopGroup()
//  val eventBus = EventBus()
//  val deviceRegister = DeviceRegister(eventBus)
//  val logger: Logger = LoggerFactory.getLogger("org.example.myhome.Launcher")
//  logger.info("Starting server...")
//  val deviceServerInit = createDeviceServerInitializer(eventBus)
//  startNettyServer(
//    8080,
//    bossGroup,
//    workerGroup,
//    deviceServerInit
//  )
  val context = AnnotationConfigApplicationContext(ApplicationContext::class.java)
  val eventBus = context.getBean("eventBus1") as EventBus
  val test = Test(eventBus)
  val ev = context.getBean("eventBus") as EventBus
  ev.post("World")
  eventBus.post("Hello")
  Thread.sleep(1000)
}
