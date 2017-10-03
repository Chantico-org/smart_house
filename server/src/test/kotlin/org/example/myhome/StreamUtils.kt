package org.example.myhome

import org.example.myhome.simp.core.SimpMessage
import org.example.myhome.simp.core.SimpMessageType
import org.example.myhome.simp.core.inferTypeFromByte
import org.example.myhome.simp.core.toInt
import org.example.myhome.utils.writeValue
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
  return String(buffer)
}

fun InputStream.readSimpMessage():SimpMessage {
  val text = this.readString()
  return SimpMessage(
    type = inferTypeFromByte(text[0].toByte()),
    body = text.substring(1)
  )
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

fun OutputStream.writeJson(obj: Any, type: SimpMessageType) {
  val data = writeValue(obj)
  this.writeString(data, type)
}
