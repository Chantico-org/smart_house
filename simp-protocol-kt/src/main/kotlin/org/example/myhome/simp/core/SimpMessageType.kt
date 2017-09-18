package org.example.myhome.simp.core

/**
 * Created by alexei on 14.06.17.
 */
enum class SimpMessageType {
  SUBSCRIBE,
  MESSAGE,
  UN_SUBSCRIBE,
  REQUEST,
  RESPONSE
}

fun inferTypeFromByte(byte: Byte): SimpMessageType {
  return SimpMessageType.values()
    .getOrNull(byte.toInt())
    ?: throw Error("Un-known byte type")
}

fun SimpMessageType.toByteString(): String {
  return String(byteArrayOf(this.ordinal.toByte()))
}

fun SimpMessageType.toInt(): Int {
  return this.ordinal
}
