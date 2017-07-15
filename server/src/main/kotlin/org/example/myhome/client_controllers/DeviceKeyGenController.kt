package org.example.myhome.client_controllers

import com.google.gson.Gson
import org.example.myhome.extension.logger
import org.example.myhome.services.DeviceCommandTranslator
import org.example.myhome.services.DeviceRegisterService
import org.springframework.web.bind.annotation.*
import java.time.Duration

data class KeyGenResponse(
  val deviceId: String,
  val deviceKey: String
)

data class KeyGenRequest(
  val deviceId: String
)

@RestController
class DeviceKeyGenController(
  val deviceRegisterService: DeviceRegisterService
) {
  companion object{
    val gson = Gson()
    val log by logger()
  }
  @GetMapping("/ping")
  fun ping() = "Test"

  @PostMapping("/key_gen")
  fun generateKey(
    @RequestBody body: String
  ): KeyGenResponse {
    log.debug("Key gen: $body")
    val request = gson.fromJson(body, KeyGenRequest::class.java)
    val deviceKey = deviceRegisterService.generateKey(request.deviceId)

    return KeyGenResponse(
      deviceId = request.deviceId,
      deviceKey = deviceKey
    )
  }

  @PostMapping("/command/{deviceId}/{state}")
  fun sendCommand(
    @PathVariable("deviceId") deviceId: String,
    @PathVariable("state") state: String
  ): Map<String, String?> {
    println(state)
    val response = deviceRegisterService
      .sendToDevice(deviceId, "/control/1", state)
      .block(Duration.ofSeconds(30))
    return mapOf(
      "response" to response
    )
  }
}
