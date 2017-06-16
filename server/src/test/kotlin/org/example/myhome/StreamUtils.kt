package org.example.myhome

import com.google.gson.Gson
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.device_server.simp.inferTypeFromByte
import org.example.myhome.device_server.simp.toInt
import java.io.InputStream
import java.io.OutputStream

fun InputStream.readString():String {
  val sizeBuffer = ByteArray(4)
  this.read(sizeBuffer)

  var length = 0
  for (i in 0..2) {
    val byte = sizeBuffer[i]
    length = length or byte.toInt()
    length = length shl 8
  }
  length = length or sizeBuffer[(sizeBuffer.size -1)].toInt()
  val buffer = ByteArray(length)
  this.read(buffer)
  return kotlin.text.String(buffer)
}

fun InputStream.readSimpMessage():SimpMessage {
  val text = this.readString()
  println(text)
  return SimpMessage(
    type = inferTypeFromByte(text[0].toByte()),
    body = text.substring(1)
  )
}

fun <T> InputStream.readJson(gson: Gson, clazz: Class<T>): T {
  return gson.fromJson(this.readString(), clazz)
}

fun OutputStream.writeString(data:String, type: SimpMessageType) {
  val length_buffer: ByteArray = ByteArray(4)
  var data_length = data.length + 1
  for (i in 3 downTo 0) {
    val byte = data_length and 255
    length_buffer[i] = byte.toByte()
    data_length = data_length shr 8
  }
  this.write(length_buffer)
  this.write(type.toInt())
  this.write(data.toByteArray())
}

fun OutputStream.writeJson(gson:Gson, obj: Any, type: SimpMessageType) {
  val data = gson.toJson(obj)
  this.writeString(data, type)
}
