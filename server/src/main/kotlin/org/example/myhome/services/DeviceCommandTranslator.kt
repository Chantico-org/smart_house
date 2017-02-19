package org.example.myhome.services

import com.google.gson.Gson
import org.example.myhome.exceptions.UnsupportedFirmwareVersion
import org.example.myhome.models.CommandMeta
import org.example.myhome.models.DeviceMetaData
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.io.InputStreamReader

@Suppress("UNCHECKED_CAST")
fun initFv0Dictionary(gson: Gson): Map<String, Map<String, Double>> {
  return gson.fromJson(
    InputStreamReader(ClassPathResource("dictionaries/FV0.json").inputStream),
    Map::class.java
  ) as Map<String, Map<String, Double>>
}

fun initDictionary(gson: Gson): Map<Int, Map<String, Map<String, Double>>> {
  return mapOf(0 to initFv0Dictionary(gson = gson))
}

@Service
class DeviceCommandTranslator {
  companion object {
    var dictionary = initDictionary(gson = Gson())
  }
  fun translateCommand(deviceMetaData: DeviceMetaData, commandMeta: CommandMeta): Int? {
    val majorVersion = deviceMetaData.firmwareVersion ushr 16
    println(majorVersion)
    return dictionary[majorVersion]
      ?.get(commandMeta.controlType)
      ?.get(commandMeta.command)
      ?.toInt()
      ?: throw UnsupportedFirmwareVersion(message = "Unknown firmware")
  }
}
