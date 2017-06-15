package org.example.myhome

import com.google.gson.Gson
import org.example.myhome.models.DeviceMetaData
import org.junit.Test
import java.net.Socket
import java.util.*

class ServerSpec {
  @Test
  fun testRegistration() {
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
    output.writeJson(gson, deviceMetaData, 3)
    println(input.readSimpMessage())
    socket.close()
  }
}
