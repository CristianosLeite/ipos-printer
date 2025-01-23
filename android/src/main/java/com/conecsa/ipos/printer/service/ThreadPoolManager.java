package com.conecsa.ipos.printer.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

  public Future<?> submitTask(Runnable task) {
    return service.submit(task);
  }

  /**
   * Create a task to run the command in the background.
   */
}
