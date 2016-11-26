import org.junit.Test;

import java.nio.ByteBuffer;

/**
 */
public class AnotherTest {
  @Test
  public void name() throws Exception {
    System.out.println(ByteBuffer.allocate(23).putInt(23).capacity());
  }
}
