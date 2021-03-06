package org.example.myhome.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper = ObjectMapper().registerKotlinModule()

fun writeValue(message: Any) =
  objectMapper.writeValueAsString(message)

inline fun <reified T: Any> readValue(content: String): T = objectMapper.readValue(content)

fun readTree(content: String) = objectMapper.readTree(content)

enum class MessageSegment(val segmentName: String) {
  BODY("body"),
  ID("id"),
  TOPIC("topic")
}

inline fun <reified T> parse(json: JsonNode, messageSegment: MessageSegment): T {
  val segment: Any = with(messageSegment) {
    when (T::class) {
      String::class -> json[segmentName].asText()
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
