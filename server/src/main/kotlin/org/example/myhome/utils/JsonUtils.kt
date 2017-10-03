package org.example.myhome.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val objectMapper = ObjectMapper().registerKotlinModule()

fun writeValue(message: Any): String = objectMapper.writeValueAsString(message)
