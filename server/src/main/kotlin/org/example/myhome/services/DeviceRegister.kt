package org.example.myhome.services

import com.google.common.eventbus.EventBus
import com.google.common.eventbus.Subscribe
import io.netty.channel.Channel
import org.example.myhome.models.Device
import org.example.myhome.models.DeviceMetaData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DeviceRegister(val eventBus: EventBus){
  val devices: Map<String, Device> = emptyMap()
  val logger: Logger = LoggerFactory.getLogger(javaClass)
  init {
      eventBus.register(this)
  }

  @Subscribe
  fun handleDeviceMetaData(deviceMetaDataEvent: Pair<DeviceMetaData, Channel>) {
    val (deviceMetaData, channel) = deviceMetaDataEvent
    logger.debug("Meta data received: $deviceMetaData")
    devices + (deviceMetaData.deviceId to Device(channel))
  }

  fun dispose() {
    eventBus.unregister(this)
  }
}
