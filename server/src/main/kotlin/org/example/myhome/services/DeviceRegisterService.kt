package org.example.myhome.services

import mu.KotlinLogging
import org.example.myhome.device_server.handlers.DeviceInteractHandler
import org.example.myhome.exceptions.DeviceNotFound
import org.example.myhome.models.DeviceMetaData
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DeviceRegisterService {
  companion object {
    val log = KotlinLogging.logger {  }
  }

  var devicesMetaData:Map<String, DeviceMetaData> = emptyMap()
  private var deviceChannels: Map<String, DeviceInteractHandler> = emptyMap()
  private var deviceKeys: Map<String, String> = mapOf(
    "test" to "test"
  )

  fun checkDevice(deviceMetaData: DeviceMetaData)
    = deviceKeys[deviceMetaData.deviceId] == deviceMetaData.deviceKey

  fun registerDevice(
    deviceMetaData: DeviceMetaData,
    deviceInteractHandler: DeviceInteractHandler
  ) {
    if (!checkDevice(deviceMetaData)) {
      log.debug("Device with ${deviceMetaData.deviceId} has wrong key")
      return
    }
    log.debug("Registered: $deviceMetaData")
    devicesMetaData += (deviceMetaData.deviceId to deviceMetaData)
    deviceChannels += (deviceMetaData.deviceId to deviceInteractHandler)
    deviceInteractHandler.channelHandlerContext?.channel()?.closeFuture()?.addListener {
      log.debug("Device channel closed ${deviceMetaData.deviceId}")
      devicesMetaData -= deviceMetaData.deviceId
      deviceChannels -= deviceMetaData.deviceId
    }
  }

  fun generateKey(deviceId: String): String {
    val deviceKey = UUID.randomUUID().toString()
    deviceKeys += (deviceId to deviceKey)
    return deviceKey
  }

  fun sendToDevice(deviceId: String, destination: String, data: String): Mono<String> {
    return deviceChannels[deviceId]
      ?.send(destination, data)
      ?: throw DeviceNotFound(deviceId)
  }
}
