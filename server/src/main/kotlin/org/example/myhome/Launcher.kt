package org.example.myhome

import org.example.myhome.device_server.DeviceServer
import org.example.myhome.utils.syncChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun main(args: Array<String>) {
  val port = 8080
  val logger: Logger = LoggerFactory.getLogger("org.example.myhome.Launcher")
  val deviceServer = DeviceServer(port)
  logger.info("Starting server...")
  val channel = deviceServer.start()?.syncChannel()
  logger.info("Server started at {}", port)
  channel?.closeFuture()?.sync()
  deviceServer.bossGroup.shutdownGracefully()
  deviceServer.workerGroup.shutdownGracefully()
}
