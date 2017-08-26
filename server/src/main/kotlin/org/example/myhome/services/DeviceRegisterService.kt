package org.example.myhome.services

import mu.KotlinLogging
import org.example.myhome.device_server.handlers.DeviceInteractHandler
import org.example.myhome.dto.DeviceMetaDataDto
import org.example.myhome.exceptions.DeviceNotFound
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DeviceRegisterService {
  companion object {
    val log = KotlinLogging.logger {  }
  }

  var devicesMetaDataDto:Map<String, DeviceMetaDataDto> = emptyMap()
  private var deviceChannels: Map<String, DeviceInteractHandler> = emptyMap()
  private var deviceKeys: Map<String, String> = mapOf(
    "test" to "test"
  )

  fun checkDevice(deviceMetaDataDto: DeviceMetaDataDto)
    = deviceKeys[deviceMetaDataDto.deviceId] == deviceMetaDataDto.deviceKey

  fun registerDevice(
    deviceMetaDataDto: DeviceMetaDataDto,
    deviceInteractHandler: DeviceInteractHandler
  ) {
    if (!checkDevice(deviceMetaDataDto)) {
      log.debug("Device with ${deviceMetaDataDto.deviceId} has wrong key")
      return
    }
    log.debug("Registered: $deviceMetaDataDto")
    devicesMetaDataDto += (deviceMetaDataDto.deviceId to deviceMetaDataDto)
    deviceChannels += (deviceMetaDataDto.deviceId to deviceInteractHandler)
    deviceInteractHandler.getCloseFuture().addListener {
      log.debug("Device channel closed ${deviceMetaDataDto.deviceId}")
      devicesMetaDataDto -= deviceMetaDataDto.deviceId
      deviceChannels -= deviceMetaDataDto.deviceId
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
