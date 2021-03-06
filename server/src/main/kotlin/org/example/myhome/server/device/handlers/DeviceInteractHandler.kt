package org.example.myhome.server.device.handlers

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import mu.KotlinLogging
import org.example.myhome.simp.core.SimpMessage
import org.example.myhome.simp.core.SimpMessageHandler
import org.example.myhome.simp.core.SimpMessageType
import org.example.myhome.utils.MessageSegment
import org.example.myhome.utils.parse
import org.example.myhome.utils.readTree
import org.example.myhome.utils.writeValue
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink
import reactor.core.scheduler.Schedulers
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class DeviceInteractHandler : SimpMessageHandler() {
  companion object {
    val log = KotlinLogging.logger { }
  }

  private val subscribersLock = ReentrantLock()

  private lateinit var channelHandlerContext: ChannelHandlerContext
  private var currentCorrelationId = Int.MIN_VALUE
  private var senderSinkMap = emptyMap<Int, MonoSink<String>>()
  private var subscriptionSinkMap = emptyMap<String, FluxSink<String>>()
  private var subscriptionCache = emptyMap<String, Flux<String>>()

  override fun channelRegistered(ctx: ChannelHandlerContext?) {
    channelHandlerContext = ctx!!
    super.channelRegistered(ctx)
  }

  override fun handleSimpMessage(ctx: ChannelHandlerContext, message: SimpMessage) {
    when (message.type) {
      SimpMessageType.MESSAGE -> handleMessage(messageBody = message.body)
      SimpMessageType.RESPONSE -> handleResponse(responseBody = message.body)
      else -> ctx.fireChannelRead(message)
    }
  }

  fun getCloseFuture(): ChannelFuture =
    channelHandlerContext.channel().closeFuture()

  private fun handleResponse(responseBody: String) {
    val json = readTree(responseBody)
    val body: String = parse(json, MessageSegment.BODY)
    val correlationId: Int = parse(json, MessageSegment.ID)
    senderSinkMap[correlationId]?.success(body)
  }

  private fun handleMessage(messageBody: String) {
    val json = readTree(messageBody)
    val topic: String = parse(json, MessageSegment.TOPIC)
    val body: String = parse(json, MessageSegment.BODY)
    subscriptionSinkMap[topic]?.next(body)
  }

  private fun createSubscriptionFlux(topic: String): Flux<String> =
    Flux.create { sink: FluxSink<String> ->
      subscribersLock.withLock {
        subscriptionSinkMap += topic to sink
      }
      val message = SimpMessage(
        type = SimpMessageType.SUBSCRIBE,
        body = "{\"topic\":\"$topic\"}"
      )
      channelHandlerContext.writeAndFlush(message)
    }.doFinally {
      //TODO send un-subscribe
      subscribersLock.withLock {
        subscriptionSinkMap -= topic
        subscriptionCache -= topic
      }
    }
      .publishOn(Schedulers.elastic())
      .publish()
      .refCount()

  fun subscribe(topic: String): Flux<String> = subscribersLock.withLock {
    if (subscriptionCache.containsKey(topic)) {
      log.debug { "Flux from cache [topic]: $topic" }
      return subscriptionCache.getValue(topic)
    }
    log.debug { "Create new Flux [topic]: $topic" }
    val source = createSubscriptionFlux(topic)
    subscriptionCache += topic to source
    return source
  }

  fun send(destination: String, body: String): Mono<String> {
    val correlationId = currentCorrelationId++
    return Mono.create { sink: MonoSink<String> ->
      // TODO: possibly use MessageSegment
      val messageBody = mapOf(
        "destination" to destination,
        "id" to correlationId,
        "body" to body
      )
      val message = SimpMessage(
        type = SimpMessageType.REQUEST,
        body = writeValue(messageBody)
      )
      senderSinkMap += correlationId to sink
      channelHandlerContext.writeAndFlush(message)
    }.doFinally {
      senderSinkMap -= correlationId
    }.publishOn(Schedulers.elastic())
  }

}
