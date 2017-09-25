package org.example.myhome.utils

import java.time.Duration
import java.time.Duration.*

val Int.seconds: Duration
  get() = ofSeconds(this.toLong())

val Int.millis: Duration
  get() = ofMillis(this.toLong())
