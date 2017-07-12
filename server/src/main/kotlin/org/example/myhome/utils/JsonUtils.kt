package org.example.myhome.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper = ObjectMapper().registerKotlinModule()

fun <T> parseJson(message: String, clazz: Class<T>)
  = objectMapper.readValue(message, clazz)

fun writeValue(message: Any) = objectMapper.writeValueAsString(message)
