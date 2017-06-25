package org.example.myhome.services

import io.netty.channel.ChannelFuture
import io.netty.util.concurrent.Promise
import org.example.myhome.device_server.handlers.DeviceInteractHandler
import org.example.myhome.models.DeviceMetaData
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceRegisterService {

  var devicesMetaData:Map<String, DeviceMetaData> = emptyMap()
  private var deviceChannels: Map<String, DeviceInteractHandler> = emptyMap()
  private var deviceKeys: Map<String, String> = emptyMap()

  fun registerDevice(deviceMetaData: DeviceMetaData, deviceInteractHandler: DeviceInteractHandler): Boolean {
    if (deviceKeys[deviceMetaData.deviceId] != deviceMetaData.deviceKey) return false
    println("Registered: $deviceMetaData")
    devicesMetaData += (deviceMetaData.deviceId to deviceMetaData)
    deviceChannels += (deviceMetaData.deviceId to deviceInteractHandler)
    return true
  }

  fun generateKey(deviceId: String): String {
    val deviceKey = UUID.randomUUID().toString()
    deviceKeys += (deviceId to deviceKey)
    return deviceKey
  }

  fun sendToDevice(deviceId: String, data: String): ChannelFuture? {
    return deviceChannels[deviceId]?.channelHandlerContext?.writeAndFlush(data)
  }

  fun readFromDevice(deviceId: String): Promise<String>? {
    return deviceChannels[deviceId]?.channelHandlerContext?.executor()?.newPromise<String>()
  }
}
