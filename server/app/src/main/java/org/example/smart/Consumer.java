package org.example.smart;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 */
public class Consumer {
  EventBus eventBus;
  public Consumer(EventBus eventBus){
    this.eventBus = eventBus;
    eventBus.register(this);
  }
  @Subscribe
  public void consume(MyConnectEvent event) {
    System.out.println("My event occurs");
  }
  public void dispose() {
    eventBus.unregister(this);
  }
}
