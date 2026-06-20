package com.conecsa.iposprinter;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Garante uma única resolução por chamada do plugin.
 *
 * <p>O serviço nativo pode disparar o callback mais de uma vez por operação (o
 * serviço recebe o callback, {@code printerPerformPrint} o recebe de novo e o
 * wrapper ainda o chama explicitamente). Este guard deixa passar apenas o
 * primeiro disparo via {@link AtomicBoolean#compareAndSet}, tornando os
 * disparos seguintes idempotentes — raiz da quebra intermitente de impressão.
 *
 * <p>Extraído como POJO para permitir teste unitário puro (sem Robolectric),
 * conforme recomendado no mapa de testes.
 */
public final class SingleShotGuard {
  private final AtomicBoolean resolved = new AtomicBoolean(false);

  /**
   * @return {@code true} apenas na primeira invocação; {@code false} nas seguintes.
   */
  public boolean tryResolve() {
    return resolved.compareAndSet(false, true);
  }
}
