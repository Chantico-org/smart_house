package org.example.myhome.client_controllers

import com.google.gson.Gson
import io.netty.util.concurrent.Promise
import org.example.myhome.exceptions.DeviceNotFound
import org.example.myhome.models.CommandMeta
import org.example.myhome.models.DeviceMetaData
import org.example.myhome.services.DeviceCommandTranslator
import org.example.myhome.services.DeviceRegisterService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

data class KeyGenResponse(
  val deviceId: String,
  val deviceKey: String
)

data class KeyGenRequest(
  val deviceId: String
)

@RestController
class DeviceKeyGenController(
  val deviceRegisterService: DeviceRegisterService,
  val deviceCommandTranslator: DeviceCommandTranslator
) {
  companion object{
    val gson = Gson()
  }
  @PostMapping("/key_gen")
  fun generateKey(
    @RequestBody body: String
  ): KeyGenResponse {
    val request = gson.fromJson(body, KeyGenRequest::class.java)
    val deviceKey = deviceRegisterService.generateKey(request.deviceId)
    return KeyGenResponse(
      deviceId = request.deviceId,
      deviceKey = deviceKey
    )
  }

  @PostMapping("/command/{deviceId}")
  fun sendCommand(
    @PathVariable("deviceId") deviceId: String,
    @RequestBody body:String
  ): Map<String, String?> {
    println(body)
    val commandMeta = gson.fromJson(body, CommandMeta::class.java)
    println("Parsed command meta: $commandMeta")
    val promise: Promise<String>? = deviceRegisterService.readFromDevice(deviceId)
    println("TEMP: Devices meta: ${deviceRegisterService.devicesMetaData}")
    val deviceMetaData: DeviceMetaData = deviceRegisterService.devicesMetaData[deviceId]
      ?: throw DeviceNotFound("Can't find device $deviceId")
    val command = deviceCommandTranslator.translateCommand(deviceMetaData = deviceMetaData, commandMeta = commandMeta)
    deviceRegisterService.sendToDevice(deviceId, gson.toJson(mapOf("command" to command)))?.sync()
    val result = promise?.sync()?.get()
    return mapOf("res" to result)
  }
}
