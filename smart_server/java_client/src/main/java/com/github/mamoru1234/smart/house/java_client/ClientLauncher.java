package com.github.mamoru1234.smart.house.java_client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.util.concurrent.Promise;

import java.util.UUID;

/**
 */
public class ClientLauncher implements Runnable{
  private static int iid=0;
  private final int id =iid++;
  @Override
  public void run() {
    Client client = new Client();
    ObjectMapper mapper = new ObjectMapper();
    try {
      client.connect("localhost", 8080).sync();
      Model model = new Model();
      model.setDeviceID(UUID.randomUUID().toString());
      Promise<?> w1 = client.write(mapper.writeValueAsString(model));
//      model.setDeviceID("Another name");
//      Promise<?> w2 = client.write(mapper.writeValueAsString(model));
      client.flush();
      w1.sync();
//      w2.sync();
      client.close().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } finally {
      try {
        System.out.println("Finishing " + id);
        client.dispose().sync();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
