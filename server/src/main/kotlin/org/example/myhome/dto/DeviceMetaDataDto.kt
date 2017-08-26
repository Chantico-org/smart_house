package org.example.myhome.dto

data class DeviceMetaDataDto(
  val deviceId: String,
  val firmwareVersion: Int,
  val deviceKey: String,
  val sensors: List<Int>,
  val controls: List<Int>
)
