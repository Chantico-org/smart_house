import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 */
public class PolygonTest {
  @Test
  public void myTest() throws Exception {
    ByteBuf b = ByteBufAllocator.DEFAULT.buffer()
      .writeBytes(ByteBuffer.allocate(4).putInt(45).array());
    System.out.println(b);
  }
}
