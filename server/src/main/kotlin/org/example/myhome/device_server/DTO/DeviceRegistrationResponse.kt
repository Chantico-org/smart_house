package org.example.myhome.device_server.DTO

import com.google.gson.annotations.SerializedName


data class DeviceRegistrationResponse(
  @SerializedName("r")
  val responseCode: Int
)
