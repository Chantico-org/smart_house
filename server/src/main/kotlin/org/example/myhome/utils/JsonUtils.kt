package org.example.myhome.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper = ObjectMapper().registerKotlinModule()

fun <T> parseJson(message: String, clazz: Class<T>) =
  objectMapper.readValue(message, clazz)

fun writeValue(message: Any) =
  objectMapper.writeValueAsString(message)

enum class MessageSegment(val segmentName: String) {
  BODY("body"),
  ID("id"),
  TOPIC("topic")
}

inline fun <reified T> parse(json: JsonNode, messageSegment: MessageSegment): T {
  val segment: Any = with(messageSegment) {
    when (T::class) {
      String::class -> json[segmentName].toString()
      Int::class -> json[segmentName].toInt()
      else -> throw RuntimeException("Parse error [expected ${T::class} in field ${segmentName}]")
    }
  }
  return segment as T
}

fun JsonNode.toInt(): Int {
  return if (this.isInt) this.asInt()
  else throw RuntimeException("Parse error [expected Int]")
}
