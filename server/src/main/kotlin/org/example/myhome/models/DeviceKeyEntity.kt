package org.example.myhome.models

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class DeviceKeyEntity(
  @Id
  var deviceId: String = "",

  var deviceKey: String = ""
)
