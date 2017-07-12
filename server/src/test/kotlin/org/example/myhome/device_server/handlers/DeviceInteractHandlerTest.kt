package org.example.myhome.device_server.handlers

import io.netty.channel.ChannelHandlerContext
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.utils.objectMapper
import org.example.myhome.utils.writeValue
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import reactor.test.StepVerifier
import java.util.*


class DeviceInteractHandlerTest {
    @Test(timeout = 10000)
    fun subscribe() {
      val handler = DeviceInteractHandler()
      val context = Mockito.mock(ChannelHandlerContext::class.java)
      val message = mapOf(
        "topic" to "test",
        "body" to UUID.randomUUID().toString()
      )
      StepVerifier.create(handler.subscribe("test").take(1))
        .then {
          handler.channelRead(context, SimpMessage(type = SimpMessageType.MESSAGE, body = writeValue(message)))
        }
        .expectNext(message["body"])
        .then {
          Mockito.verifyNoMoreInteractions(context)
        }
        .verifyComplete()
    }

    @Test
    fun send() {
      val handler = DeviceInteractHandler()
      val captor = ArgumentCaptor.forClass(SimpMessage::class.java)
      val context = Mockito.mock(ChannelHandlerContext::class.java)
      handler.channelRegistered(context)
      val body = UUID.randomUUID().toString()
      StepVerifier.create(handler.send("test", ""))
        .then {
          Mockito.verify(context).writeAndFlush(captor.capture())
          val config = objectMapper.readTree(captor.value.body)
          val message = mapOf(
            "id" to config["id"].asInt(),
            "body" to body
          )
          handler.channelRead(context, SimpMessage(type = SimpMessageType.RESPONSE, body = writeValue(message)))
        }
        .expectNext(body)
        .verifyComplete()
    }

}
