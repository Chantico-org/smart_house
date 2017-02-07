package org.example.myhome

import com.google.gson.Gson
import io.kotlintest.specs.StringSpec
import org.example.myhome.models.DeviceMetaData
import java.net.Socket

class ServerSpec : StringSpec() {
  init {
      "device into stage" {
        val socket = Socket("localhost", 7080)
        val gson = Gson()
        val output = socket.outputStream
        val deviceMetaData = DeviceMetaData(
          deviceId = "Random String",
          firmwareVersion = 0,
          sensors = listOf(1, 2, 3),
          controls = listOf(0, 1, 3)
        )
        output.writeJson(gson, deviceMetaData)
        socket.close()
      }
  }
}
