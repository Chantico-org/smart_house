package org.example.myhome

import com.google.gson.Gson
import feign.Feign
import feign.gson.GsonDecoder
import feign.gson.GsonEncoder
import io.kotlintest.specs.StringSpec
import org.example.myhome.client_controllers.KeyGenRequest
import org.example.myhome.device_server.DTO.DeviceRegistrationResponse
import org.example.myhome.models.CommandMeta
import org.example.myhome.models.DeviceMetaData
import java.net.Socket
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ServerSpec : StringSpec() {
  init {
    "device into system wrong key" {
      val socket = Socket("localhost", 7080)
      val gson = Gson()
      val input = socket.inputStream
      val output = socket.outputStream
      val deviceMetaData = DeviceMetaData(
        deviceId = UUID.randomUUID().toString(),
        deviceKey = UUID.randomUUID().toString(),
        firmwareVersion = 0,
        sensors = listOf(1, 2, 3),
        controls = listOf(0, 1, 3)
      )
      output.writeJson(gson, deviceMetaData)
      input.readJson(gson, DeviceRegistrationResponse::class.java) shouldEqual DeviceRegistrationResponse(1)
      socket.close()
    }
    "registration flow" {
      val client = Feign.builder()
        .encoder(GsonEncoder())
        .decoder(GsonDecoder())
        .target(RegisterClient::class.java, "http://localhost:6000")
      val deviceId = UUID.randomUUID().toString()
      val response = client.generateKey(KeyGenRequest(deviceId = deviceId))
      val socket = Socket("localhost", 7080)
      val gson = Gson()
      val input = socket.inputStream
      val output = socket.outputStream
      val deviceMetaData = DeviceMetaData(
        deviceId = deviceId,
        deviceKey = response.deviceKey,
        firmwareVersion = 1,
        sensors = listOf(1, 2, 3),
        controls = listOf(0, 1, 3)
      )
      output.writeJson(gson, deviceMetaData)
      input.readJson(gson, DeviceRegistrationResponse::class.java) shouldEqual DeviceRegistrationResponse(0)
      val executor = Executors.newFixedThreadPool(2)
      val text = "Hello world"
      val responseText = "Yes I am here"
      executor.execute {
        println("start 1")
        val commandMeta = CommandMeta(command = "TURN_ON", controlType = "LIGHT")
        client.sendCommand(deviceId, commandMeta)shouldEqual mapOf("res" to responseText)
        println("finish 1")
      }
      executor.execute {
        println("start 2")
        input.readString() shouldEqual "{\"command\":1}"
        output.writeString(responseText)
        println("finish 2")
      }
      println("Before it")
      executor.shutdownNow()
      executor.awaitTermination(1, TimeUnit.DAYS)
      println("After await")
      socket.close()
    }
  }
}
