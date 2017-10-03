package org.example.myhome.client_controllers

import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging
import org.example.myhome.services.DeviceRegisterService
import org.example.myhome.utils.objectMapper
import org.springframework.web.bind.annotation.*
import java.time.Duration
import java.util.*

data class KeyGenResponse(
  val deviceId: UUID,
  val deviceKey: String
)

data class KeyGenRequest(
  val deviceId: UUID
)

@RestController
class DeviceKeyGenController(
  private val deviceRegisterService: DeviceRegisterService
) {
  companion object{
    val log = KotlinLogging.logger {  }
  }
  @GetMapping("/ping")
  fun ping() = "Test"

  @PostMapping("/key_gen")
  fun generateKey(
    @RequestBody body: String
  ): KeyGenResponse {
    log.debug("Key gen: $body")
    val request: KeyGenRequest = objectMapper.readValue(body)
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
