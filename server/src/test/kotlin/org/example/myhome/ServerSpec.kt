package org.example.myhome

import com.fasterxml.jackson.databind.ObjectMapper
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.models.DeviceMetaData
import org.junit.Test
import java.net.Socket
import java.util.*

class ServerSpec {
  @Test
  fun testRegistration() {
    val socket = Socket("192.168.0.104", 7080)
    println(socket.isConnected)
    val input = socket.inputStream
    val output = socket.outputStream
    val deviceMetaData = DeviceMetaData(
      deviceId = UUID.randomUUID().toString(),
      deviceKey = UUID.randomUUID().toString(),
      firmwareVersion = 0,
      sensors = listOf(1, 2, 3),
      controls = listOf(0, 1, 3)
    )
    output.writeJson(deviceMetaData, SimpMessageType.REQUEST)
    println(input.readSimpMessage())
    println(input.readSimpMessage())
    output.writeJson(mapOf(
      "topic" to "/test",
      "body" to "My body"
    ), SimpMessageType.MESSAGE)
    output.writeJson(mapOf(
      "topic" to "/test",
      "body" to "My body"
    ), SimpMessageType.MESSAGE)
    socket.close()
  }

  @Test
  fun testSend() {
    val socket = Socket("localhost", 7080)
    val objectMapper = ObjectMapper()
    val input = socket.inputStream
    val output = socket.outputStream
    val deviceMetaData = DeviceMetaData(
      deviceId = UUID.randomUUID().toString(),
      deviceKey = UUID.randomUUID().toString(),
      firmwareVersion = 0,
      sensors = listOf(1, 2, 3),
      controls = listOf(0, 1, 3)
    )
    output.writeJson(deviceMetaData, SimpMessageType.REQUEST)
    println(input.readSimpMessage())
    val message = input.readSimpMessage()
    println(message)
    val id = objectMapper.readTree(message.body)["id"].asText()
    println(id)
    output.writeJson(mapOf(
      "destination" to "/temp",
      "id" to id,
      "body" to "My cool body"
    ), SimpMessageType.RESPONSE)

    socket.close()
  }
}
