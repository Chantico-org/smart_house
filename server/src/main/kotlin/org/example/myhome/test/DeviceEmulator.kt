package org.example.myhome.test

import com.google.gson.Gson
import org.example.myhome.models.DeviceMetaData
import java.io.OutputStream
import java.net.Socket
import java.util.*

fun writeToStream(output: OutputStream, data: String) {
  output.write(data.length)
  for (char in data) {
    output.write(char.toInt())
  }
}

fun main(args: Array<String>) {
  val socket = Socket("localhost", 8080)
  val gson = Gson()
  val output = socket.outputStream
  val deviceMetaData = DeviceMetaData(
    deviceId = UUID.randomUUID().toString(),
    firmwareVersion = 0,
    sensors = listOf(1, 2, 3),
    controls = listOf(0, 1, 3)
  )
  writeToStream(output, gson.toJson(deviceMetaData))
  output.close()
}
