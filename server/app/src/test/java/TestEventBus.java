import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.example.smart.Consumer;
import org.example.smart.MyConnectEvent;
import org.junit.Test;

import java.util.concurrent.Executors;

/**
 */
public class TestEventBus {
  @Test
  public void testIt() {
    EventBus eventBus = new AsyncEventBus(Executors.newSingleThreadExecutor());
    Consumer consumer = new Consumer(eventBus);
    eventBus.post(new MyConnectEvent());
    consumer.dispose();
//    consumer.dispose();
  }
}
