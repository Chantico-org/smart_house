package org.example.myhome.services

import org.example.myhome.device_server.handlers.DeviceInteractHandler
import org.example.myhome.models.DeviceMetaData
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeviceRegisterService {
  var devicesMetaData:Map<String, DeviceMetaData> = emptyMap()
  private var deviceChannels: Map<String, DeviceInteractHandler> = emptyMap()
  private val logger: Logger = LoggerFactory.getLogger(javaClass)

  fun registerDevice(deviceMetaData: DeviceMetaData, deviceInteractHandler: DeviceInteractHandler) {
    devicesMetaData += (deviceMetaData.deviceId to deviceMetaData)
    deviceChannels += (deviceMetaData.deviceId to deviceInteractHandler)
  }
}
