package org.example.myhome

import io.kotlintest.specs.StringSpec
import org.example.myhome.models.CommandMeta
import org.example.myhome.models.DeviceMetaData
import org.example.myhome.services.DeviceCommandTranslator
import java.util.*

class DeviceCommandTranslatorTest: StringSpec() {
  init {
    "Test this shit" {
      val service = DeviceCommandTranslator()
      val deviceMeta = DeviceMetaData(
        deviceId = UUID.randomUUID().toString(),
        deviceKey = UUID.randomUUID().toString(),
        firmwareVersion = 0,
        controls = emptyList(),
        sensors = emptyList()
      )
      val commandMeta = CommandMeta(
        command = "TURN_ON",
        controlType = "LIGHT"
      )
      val result = service.translateCommand(deviceMeta, commandMeta)
      result shouldEqual 1
    }
  }
}
