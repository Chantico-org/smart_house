package org.example.myhome.device_server.simp

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec

/**
 * Created by alexei on 15.06.17.
 */
class SimpCodec: MessageToMessageCodec<String, SimpMessage>() {
  override fun decode(ctx: ChannelHandlerContext?, msg: String?, out: MutableList<Any>?) {
    if (msg == null || out == null) return
    try {
      val type = inferTypeFromByte(msg[0].toByte())
      out.add(SimpMessage(type = type, body = msg.substring(1)))
    } catch(e: Error) {
      ctx?.close();
    }
  }

  override fun encode(ctx: ChannelHandlerContext?, msg: SimpMessage?, out: MutableList<Any>?) {
    println("Encoding")
    if (msg == null || out == null) return
    val text = msg.type.toByteString() + msg.body
    out.add(text)
  }
}
