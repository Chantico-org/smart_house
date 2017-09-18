package org.example.myhome.simp.core

import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import java.nio.CharBuffer
import java.nio.charset.Charset

/**
 * Created by alexei on 15.06.17.
 */
class SimpCodec(
  val charset: Charset = Charset.defaultCharset()
): MessageToMessageCodec<ByteBuf, SimpMessage>() {
  override fun decode(ctx: ChannelHandlerContext?, msg: ByteBuf?, out: MutableList<Any>?) {
    if (msg == null || out == null || ctx == null) return
    try {
      val type = inferTypeFromByte(msg.readByte())
      out.add(SimpMessage(type = type, body = msg.toString(charset)))
    } catch(e: Error) {
      ctx.close()
    }
  }

  override fun encode(ctx: ChannelHandlerContext?, msg: SimpMessage?, out: MutableList<Any>?) {
    if (ctx == null) {
      return
    }
    if (msg == null || out == null) return
    val text = msg.type.toByteString() + msg.body
    out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(text), charset))
  }
}
