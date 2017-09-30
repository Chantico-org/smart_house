package org.example.myhome

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.example.myhome.dto.DeviceMetaDataDto
import org.example.myhome.simp.core.SimpMessageType
import org.junit.Test
import java.net.Socket
import java.util.*

//TODO move it to separate module with integration tests
class ServerSpec {
  @Test(timeout = 1000)
  fun testRegistration() {
    val socket = Socket("localhost", 7080)
    val inputStream = socket.inputStream
    val outputStream = socket.outputStream
    val deviceMetaData = DeviceMetaDataDto(
      deviceId = UUID.randomUUID().toString(),
      deviceKey = UUID.randomUUID().toString(),
      firmwareVersion = 0,
      sensors = listOf(1, 2, 3),
      controls = listOf(0, 1, 3)
    )
    outputStream.writeJson(deviceMetaData, SimpMessageType.REQUEST)
    val response = inputStream.readSimpMessage()
    assertThat(response.type, equalTo(SimpMessageType.RESPONSE))
    assertThat(response.body, equalTo("NO"))
//    assert that client is disconnected
    assertThat(inputStream.read(), equalTo(-1))
    socket.close()
  }
}
