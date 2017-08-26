package org.example.myhome.entity

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

/**
* Created by Alexei Gontar.
*/
@Entity
data class DeviceKeyEntity(
  @Id
  var deviceId: String = UUID.randomUUID().toString(),

  var deviceKey: String = ""
)
