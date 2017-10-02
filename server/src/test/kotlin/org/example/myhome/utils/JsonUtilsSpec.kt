package org.example.myhome.utils

import org.assertj.core.api.Assertions.assertThat
import org.testng.annotations.Test

class JsonUtilsSpec {

  private val someBody = """
      |{
        |"somefield":"somevalue"
      |}
  """.trimMargin().replace("\n", "")

  private val topicName = "/test"

  private val someJson = """
      |{
        |"id":42,
        |"body":$someBody,
        |"topic":"$topicName"
      |}
  """.trimMargin().replace("\n", "")

  private val json = objectMapper.readTree(someJson)

  @Test
  fun parseBody() {
    val body: String = parse(json, MessageSegment.BODY)
    assertThat(body).isEqualTo(someBody)
  }

  @Test
  fun parseId() {
    val body: Int = parse(json, MessageSegment.ID)
    assertThat(body).isEqualTo(42)
  }

  @Test(expectedExceptions = arrayOf(RuntimeException::class))
  fun parseBody_failLong() {
    val body: Long = parse(json, MessageSegment.ID)
    assertThat(body).isEqualTo(42L)
  }
}
