package com.conecsa.iposprinter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Regressão da race da impressora: o guard que garante UMA resolução por chamada.
 * Cada PluginCall recebe seu próprio {@link SingleShotGuard}; só o primeiro
 * disparo do callback nativo resolve a chamada, os demais são ignorados.
 */
public class SingleShotGuardTest {

  @Test
  public void permiteExatamenteUmaResolucao() {
    SingleShotGuard guard = new SingleShotGuard();
    assertTrue("o primeiro disparo deve resolver", guard.tryResolve());
    assertFalse("o segundo disparo (tardio) não deve resolver", guard.tryResolve());
    assertFalse("disparos seguintes continuam ignorados", guard.tryResolve());
  }

  @Test
  public void guardsIndependentesNaoVazamEntreChamadas() {
    // Simula duas operações sequenciais (A e B): cada uma tem seu guard.
    SingleShotGuard a = new SingleShotGuard();
    SingleShotGuard b = new SingleShotGuard();

    assertTrue(a.tryResolve());   // A resolve
    assertTrue(b.tryResolve());   // B resolve, independente de A
    assertFalse(a.tryResolve());  // disparo tardio de A não afeta nada
    assertFalse(b.tryResolve());
  }

  @Test
  public void ehThreadSafe_apenasUmVencedorEntreThreads() throws InterruptedException {
    final SingleShotGuard guard = new SingleShotGuard();
    final int threads = 50;
    final AtomicInteger winners = new AtomicInteger(0);
    final CountDownLatch start = new CountDownLatch(1);
    final CountDownLatch done = new CountDownLatch(threads);
    final ExecutorService pool = Executors.newFixedThreadPool(threads);

    for (int i = 0; i < threads; i++) {
      pool.execute(() -> {
        try {
          start.await();
        } catch (InterruptedException ignored) {
          Thread.currentThread().interrupt();
        }
        if (guard.tryResolve()) {
          winners.incrementAndGet();
        }
        done.countDown();
      });
    }

    start.countDown(); // libera todas as threads ao mesmo tempo
    done.await(5, TimeUnit.SECONDS);
    pool.shutdownNow();

    assertEquals("apenas uma thread pode resolver a chamada", 1, winners.get());
  }
}
