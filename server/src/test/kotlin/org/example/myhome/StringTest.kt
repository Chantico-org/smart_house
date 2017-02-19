package org.example.myhome

import io.kotlintest.specs.StringSpec

class StringTest: StringSpec() {
  init {
      "test null"{
        1 shr 16 shouldBe 0
      }
  }
}
