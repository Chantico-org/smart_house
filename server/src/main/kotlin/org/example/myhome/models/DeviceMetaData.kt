package org.example.myhome.models

data class DeviceMetaData (
  val deviceId: String,
  val firmwareVersion: Int,
  val deviceKey: String,
  val sensors: List<Int>,
  val controls: List<Int>
)
