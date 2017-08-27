package org.example.myhome.services

import mu.KotlinLogging
import org.example.myhome.dao.DeviceKeyDao
import org.example.myhome.device_server.handlers.DeviceInteractHandler
import org.example.myhome.dto.DeviceMetaDataDto
import org.example.myhome.entity.DeviceKeyEntity
import org.example.myhome.exceptions.DeviceNotFound
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class DeviceRegisterService(
  private val deviceKeyDao: DeviceKeyDao
) {
  companion object {
    private val log = KotlinLogging.logger {  }
  }

  var devicesMetaDataDto:Map<String, DeviceMetaDataDto> = emptyMap()
  private var deviceChannels: Map<String, DeviceInteractHandler> = emptyMap()

  fun checkDevice(deviceMetaDataDto: DeviceMetaDataDto) = deviceKeyDao.findOne(deviceMetaDataDto.deviceId)
    ?.let {
      it.deviceKey == deviceMetaDataDto.deviceKey
    }
    ?: false

  fun registerDevice(
    deviceMetaDataDto: DeviceMetaDataDto,
    deviceInteractHandler: DeviceInteractHandler
  ) {
    if (!checkDevice(deviceMetaDataDto)) {
      log.debug {
        "Device with ${deviceMetaDataDto.deviceId} has wrong key"
      }
      return
    }
    log.debug {
      "Registered device: $deviceMetaDataDto"
    }
    devicesMetaDataDto += (deviceMetaDataDto.deviceId to deviceMetaDataDto)
    deviceChannels += (deviceMetaDataDto.deviceId to deviceInteractHandler)
    deviceInteractHandler.getCloseFuture().addListener {
      log.debug { "Device channel closed ${deviceMetaDataDto.deviceId}" }
      devicesMetaDataDto -= deviceMetaDataDto.deviceId
      deviceChannels -= deviceMetaDataDto.deviceId
    }
  }

  fun generateKey(deviceId: UUID): String {
    val deviceKeyEntity = DeviceKeyEntity(
      deviceId = deviceId.toString(),
      deviceKey = UUID.randomUUID().toString()
    )
    deviceKeyDao.save(deviceKeyEntity)
    return deviceKeyEntity.deviceKey
  }

  fun sendToDevice(deviceId: String, destination: String, data: String): Mono<String> {
    return deviceChannels[deviceId]
      ?.send(destination, data)
      ?: throw DeviceNotFound(deviceId)
  }
}
