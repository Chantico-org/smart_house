package org.example.myhome

import com.google.common.eventbus.EventBus
import io.netty.channel.nio.NioEventLoopGroup
import mu.KotlinLogging
import org.example.myhome.dao.DeviceKeyDao
import org.example.myhome.entity.DeviceKeyEntity
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Profile

@EnableAutoConfiguration
@ComponentScan
open class ApplicationContext {

  companion object {
      val log = KotlinLogging.logger {  }
  }

  @Bean("eventBus")
  open fun eventBus():EventBus = EventBus()

  @Bean("bossGroup")
  open fun bossGroup(): NioEventLoopGroup = NioEventLoopGroup()

  @Bean
  @Profile("dev")
  open fun run(
    deviceKeyDao: DeviceKeyDao
  ) = CommandLineRunner {
    log.debug {
      "Dev is running"
    }
    val testDeviceID = "00000000-0000-0000-0000-000000000000"
    val testDeviceKey = "00000000-0000-0000-0000-000000000000"
    if (deviceKeyDao.findOne(testDeviceID) == null) {
      log.debug { "Test device key added" }
      deviceKeyDao.save(DeviceKeyEntity(
        deviceId = testDeviceID,
        deviceKey = testDeviceKey
      ))
    }
    log.debug {
      "Total deviceKeys: ${deviceKeyDao.count()}"
    }
  }

  @Bean("workerGroup")
  open fun workerGroup(): NioEventLoopGroup = NioEventLoopGroup()
}

fun main(args: Array<String>) {
  SpringApplication.run(ApplicationContext::class.java)
}
