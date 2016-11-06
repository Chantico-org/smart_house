package org.example.smart.converters;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import org.example.smart.models.SensorValue;

import java.util.List;

/**
 */
public class SensorValueConverter {
  private static byte MARKER = 0x11;
  public static class SensorValueEncoder extends MessageToByteEncoder<SensorValue> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      System.out.println("Encoder added");
      super.handlerAdded(ctx);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, SensorValue msg, ByteBuf out) throws Exception {
      System.out.println("encode");
      out.writeByte(MARKER);
    }
  }
  public static class SensorValueDecoder extends ByteToMessageDecoder {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
      System.out.println("decoder added");
      super.handlerAdded(ctx);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
      System.out.println("decode");
      byte b = in.readByte();
      System.out.println(b);
      if (b == MARKER) {
        System.out.println("Correct");
        return;
      }
      ctx.fireChannelRead(in);
    }
  }
}
