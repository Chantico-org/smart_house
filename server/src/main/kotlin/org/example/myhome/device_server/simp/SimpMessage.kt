package org.example.myhome.device_server.simp

/**
 * Created by alexei on 14.06.17.
 */
data class SimpMessage(
  val body: String,
  val type: SimpMessageType
)
