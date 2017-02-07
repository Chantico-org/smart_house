package org.example.myhome

import com.google.gson.Gson
import java.io.InputStream
import java.io.OutputStream

fun <T> InputStream.readJson(gson: Gson, clazz: Class<T>): T {
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
  return gson.fromJson(kotlin.text.String(buffer), clazz)
}

fun OutputStream.writeJson(gson:Gson, obj: Any) {
  val data = gson.toJson(obj)
  val length_buffer: ByteArray = ByteArray(4)
  var data_length = data.length
  for (i in 3 downTo 0) {
    val byte = data_length and 255
    length_buffer[i] = byte.toByte()
    data_length = data_length shr 8
  }
  this.write(length_buffer)
  this.write(data.toByteArray())
}
