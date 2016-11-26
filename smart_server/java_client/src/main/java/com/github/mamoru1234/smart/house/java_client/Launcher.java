package com.github.mamoru1234.smart.house.java_client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 */
public class Launcher {
  static ExecutorService executor = Executors.newFixedThreadPool(4);
  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 4; i++) {
      executor.execute(new ClientLauncher());
      System.out.println("next");
    }
    executor.shutdown();
  }
}
