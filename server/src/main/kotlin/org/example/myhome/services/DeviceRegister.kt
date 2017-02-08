package org.example.myhome.services

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import org.example.myhome.models.Device
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import kotlin.reflect.jvm.internal.impl.javax.inject.Inject

@Service
class DeviceRegister(
  @Inject
  @Qualifier("eventBus")
  val eventBus: EventBus
){
  val devices: Map<String, Device> = emptyMap()
  val logger: Logger = LoggerFactory.getLogger(javaClass)
  init {
      eventBus.register(this)
  }

  @Subscribe
  fun handleDevice(device: Device) {
    logger.debug("Meta data received: ${device.deviceMetaData}")
    devices + (device.deviceMetaData.deviceId to device)
  }

  fun dispose() {
    eventBus.unregister(this)
  }
}
