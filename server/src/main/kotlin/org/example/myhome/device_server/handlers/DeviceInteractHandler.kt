package org.example.myhome.device_server.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.example.myhome.device_server.simp.SimpMessage
import org.example.myhome.device_server.simp.SimpMessageType
import org.example.myhome.extension.logger
import org.example.myhome.utils.objectMapper
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.scheduler.Schedulers

class DeviceInteractHandler : ChannelInboundHandlerAdapter() {
  companion object {
    val log by logger()
  }
  var currentCorrelationId = Int.MIN_VALUE
  var channelHandlerContext: ChannelHandlerContext? = null
  var senderMap = emptyMap<Int, MonoSink<String>>()
  var subscriptionMap = emptyMap<String, FluxSink<String>>()

  override fun channelRegistered(ctx: ChannelHandlerContext?) {
    channelHandlerContext = ctx
    super.channelRegistered(ctx)
  }

  override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
    if (msg !is SimpMessage) return
    val config = objectMapper.readTree(msg.body)
    when (msg.type) {
      SimpMessageType.MESSAGE -> {
        val topic = config["topic"]
          ?.asText()
          ?: ""
        val body = config["body"]
          ?.asText()
          ?: ""
        subscriptionMap[topic]?.next(body)
      }
      SimpMessageType.RESPONSE -> {
        val body = config["body"]
          ?.asText()
          ?: ""
        val correlationId = config["id"]
          ?.asInt()
          ?: 0
        println("$$$$#$$$$$$#")
        println("$$$$#$$$$$$#")
        println("$$$$#$$$$$$#")
        println(correlationId)
        println(senderMap)
        senderMap[correlationId]?.success(body)
      }
      else -> {
        log.error("Unknown message type ${msg.type}")
      }
    }
  }

  fun subscribe(destination: String): Flux<String> {
    return Flux.create {
      sink: FluxSink<String> ->
      subscriptionMap += destination to sink
      val message = SimpMessage(
        type = SimpMessageType.SUBSCRIBE,
        body = "{\"destination\":\"$destination\"}"
      )
      channelHandlerContext
        ?.writeAndFlush(message)

    }.publishOn(Schedulers.elastic())
  }

  fun send(destination: String, body: String): Mono<String> {
    return Mono.create {
      sink: MonoSink<String> ->
      val correlationId = currentCorrelationId++
      val node = objectMapper.createObjectNode()
      node.put("destination", destination)
      node.put("id", correlationId)
      node.put("body", body)
      val message = SimpMessage(
        type = SimpMessageType.REQUEST,
        body = node.toString()
      )
      senderMap += correlationId to sink
      println(message)
      channelHandlerContext
        ?.writeAndFlush(message)
    }.publishOn(Schedulers.elastic())
  }
}
