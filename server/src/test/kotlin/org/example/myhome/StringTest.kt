package org.example.myhome

import io.kotlintest.specs.StringSpec

class StringTest: StringSpec() {
  init {
      "test null"{
        var list:List<String> = listOf("fefwef")
        list.firstOrNull() shouldBe "fefwef"
        list = list.drop(1)
        list.size shouldBe 0
        list = list.drop(1)
        list.size shouldBe 0
      }
  }
}
