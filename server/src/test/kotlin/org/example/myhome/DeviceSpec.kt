package org.example.myhome

import com.google.gson.Gson
import io.kotlintest.specs.StringSpec
import org.example.myhome.device_server.DTO.DeviceRegistrationResponse
import org.example.myhome.models.DeviceMetaData
import java.net.ServerSocket
import java.util.*

class DeviceSpec : StringSpec() {
  init {
    "Device introduction" {
      val server = ServerSocket(7080)
      val client = server.accept()
      val gson = Gson()
      val deviceMetaData = DeviceMetaData(
        deviceId = "Random String",
        deviceKey = UUID.randomUUID().toString(),
        firmwareVersion = 0,
        sensors = listOf(1, 2, 3),
        controls = listOf(0, 1, 3)
      )
      Thread.sleep(1000)
      val metaData = client.inputStream.readJson(gson, DeviceMetaData::class.java)
      println(metaData)
      client.outputStream.writeJson(gson, DeviceRegistrationResponse(0))
      Thread.sleep(1000)
    }
  }
}
