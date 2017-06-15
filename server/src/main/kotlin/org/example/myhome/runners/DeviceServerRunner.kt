package org.example.myhome.runners

import org.example.myhome.device_server.DeviceServer
import org.example.myhome.utils.syncChannel
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.ContextStartedEvent
import org.springframework.stereotype.Component

/**
 * Created by alexei on 14.06.17.
 */
@Component
class DeviceServerRunner(
  private val deviceServer: DeviceServer
): ApplicationListener<ContextRefreshedEvent> {
  override fun onApplicationEvent(event: ContextRefreshedEvent?) {
    deviceServer.start()?.sync()
  }
}
