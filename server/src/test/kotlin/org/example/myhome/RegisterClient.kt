package org.example.myhome

import feign.Headers
import feign.Param
import feign.RequestLine
import org.example.myhome.client_controllers.KeyGenRequest
import org.example.myhome.client_controllers.KeyGenResponse
import org.example.myhome.models.CommandMeta

/**
 */
interface RegisterClient {
  @RequestLine("POST /key_gen")
  @Headers("Content-Type: application/json")
  fun generateKey(request: KeyGenRequest): KeyGenResponse

  @RequestLine("POST /command/{deviceId}")
  @Headers("Content-Type: application/json")
  fun sendCommand(
    @Param("deviceId") deviceId: String,
    body: CommandMeta
  ): Map<String, String>
}
