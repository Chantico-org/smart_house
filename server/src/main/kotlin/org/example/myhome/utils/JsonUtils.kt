package org.example.myhome.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper = ObjectMapper().registerKotlinModule()

fun <T> parseJson(message: String, clazz: Class<T>) =
  objectMapper.readValue(message, clazz)

fun writeValue(message: Any) =
  objectMapper.writeValueAsString(message)

enum class MessageSegment(val type: Class<*>, val segmentName: String) {
  BODY(String::class.java, "body"),
  ID(Int::class.java, "id"),
  TOPIC(String::class.java, "topic")
}

inline fun <reified T> parse(responseBody: String, segment: MessageSegment): T? {
  val config = objectMapper.readTree(responseBody)
  val any: Any? = when (segment.type) {
    String::class.java -> config[segment.segmentName]?.asText()
    Int::class.java -> config[segment.segmentName]?.asInt()
    else -> null
  }
  return any as T?
}
