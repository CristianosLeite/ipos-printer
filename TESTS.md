# TESTS — ipos-printer (mapa de casos de teste unitário)

> Mapa de casos para a próxima etapa (implementar os testes). Classificação:
> **[REG]** regressão de bug do TODO · **[CHAR]** caracterização · **[PURO]** lógica pura ·
> **[REFAC]** exige refatorar para testabilidade antes (ver `TODO.md`).

## Cobertura atual
`android/src/test/java/com/conecsa/iposprinter/IPosPrinterTest.java` (JUnit + Mockito, ~40 casos)
já cobre o **wrapper** `IPosPrinter`: guardas de serviço desconectado, validação de entrada,
mapeamento de status e delegação assíncrona via `ThreadPoolManager` (asserts com `timeout()`).
**Não cobre** o `IPosPrinterPlugin` (a race condition) nem o travamento da fila — foco deste mapa.

## Lacunas a cobrir (críticas — alinhadas ao `TODO.md`)

### `IPosPrinterPlugin` — race do `PluginCall`/`JSObject` (novo arquivo de teste)
Arquivo sugerido: `android/src/test/java/com/conecsa/iposprinter/IPosPrinterPluginTest.java`
(Robolectric, pois `Plugin`/`PluginCall` dependem do runtime Capacitor; injetar `IPosPrinter`
mock via reflexão no campo `implementation`, como `IPosPrinterTest` faz com o serviço).

- [REG] **Uma resolução por chamada**: ao invocar um `@PluginMethod` e o callback nativo disparar,
  exatamente **um** `PluginCall.resolve(...)` ocorre para aquela chamada. Hoje o callback nativo
  dispara mais de uma vez por operação (`IPosPrinter.java:347-538`), o que com o `call`/`r`
  compartilhados (`IPosPrinterPlugin.java:21-22,49-64`) pode resolver a chamada errada.
- [REG] **Sem vazamento entre chamadas sequenciais**: invocar A e depois B; um disparo tardio do
  callback de A **não** resolve o `PluginCall` de B (correlação por chamada, não por `call==null`).
- [REG] **`JSObject` não compartilhado**: o payload resolvido de A não é sobrescrito pelo de B
  (hoje ambos usam o mesmo `r`).
- [REG] **Erro propagado**: quando o serviço está indisponível (`bindService` falhou /
  `mIPosPrinterService==null`), a chamada faz `reject`/erro em vez de não resolver nunca
  (`IPosPrinter.java:182-188` retorna em silêncio → promise pendurada).

### `IPosPrinter` — fila assíncrona sem timeout
Estende `IPosPrinterTest.java` (mesmo padrão de mock + `ThreadPoolManager`).

- [REG] **Operação travada não bloqueia a fila**: simular `mIPosPrinterService.*` que demora/trava;
  após o timeout, a operação resolve/rejeita e a **próxima** operação da fila ainda executa
  (`IPosPrinter.java:137-175`).
- [CHAR] **Ordem FIFO preservada**: enfileirar N operações e verificar a ordem de delegação atual
  (fixa o comportamento antes de mexer no ciclo de vida/`stopSelf`).
- [REG] **`stopSelf` não encerra no meio de um lote** previsível (`IPosPrinter.java:149-154`).

## Notas de testabilidade
- `IPosPrinterPlugin` precisa de Robolectric (ou teste instrumentado) por causa do `Plugin` base do
  Capacitor; alternativa é extrair a lógica de correlação de chamadas para uma classe POJO testável
  com JUnit puro — recomendado e alinhado ao `[DECISÃO]` do TODO.
- Manter `testOptions.unitTests.returnDefaultValues = true` (já usado) para no-ops de `Log`/`Service`.

## Execução (na etapa de implementação)
- `cd android && ./gradlew test` (ou `npm run verify:android`).
