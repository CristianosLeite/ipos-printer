package com.conecsa.iposprinter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
  private final ExecutorService service;

  private ThreadPoolManager() {
    int num = Runtime.getRuntime().availableProcessors() * 20;
    service = Executors.newFixedThreadPool(num);
  }

  private static final ThreadPoolManager manager = new ThreadPoolManager();

  public static ThreadPoolManager getInstance() {
    return manager;
  }

  public void executeTask(Runnable runnable) {
    service.execute(runnable);
  }
}
