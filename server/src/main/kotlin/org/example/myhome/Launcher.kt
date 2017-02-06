package org.example.myhome

import com.google.common.eventbus.EventBus
import io.netty.channel.nio.NioEventLoopGroup
import org.example.myhome.device_server.createDeviceServerInitializer
import org.example.myhome.server.startNettyServer
import org.example.myhome.services.DeviceRegister
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
  val bossGroup = NioEventLoopGroup()
  val workerGroup = NioEventLoopGroup()
  val eventBus = EventBus()
  val deviceRegister = DeviceRegister(eventBus)
  val logger: Logger = LoggerFactory.getLogger("org.example.myhome.Launcher")
  logger.info("Starting server...")
  val deviceServerInit = createDeviceServerInitializer(eventBus)
  startNettyServer(
    8080,
    bossGroup,
    workerGroup,
    deviceServerInit
  )
}
