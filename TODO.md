# TODO — ipos-printer (plugin de impressão do palm)

> **Causa principal das falhas relatadas.** "A app quebra ao final do teste (intermitente)" e
> "impressora falha durante as impressões, precisando fechar/reabrir para reimprimir" têm origem
> aqui. Itens marcados **[DECISÃO]** mudam comportamento e exigem aval antes de implementar.
> Reconfirme os números de linha antes de editar.

## Causa-raiz (resumo técnico)
A camada Capacitor (`IPosPrinterPlugin.java`) guarda **um único** `PluginCall call` e **um único**
`JSObject r`, sobrescritos a cada `@PluginMethod`. A camada nativa (`IPosPrinter.java`) invoca o
callback **mais de uma vez por chamada** (o serviço subjacente chama o callback, e o wrapper ainda
chama `printerPerformPrint(...)` — que passa o mesmo callback — e depois `callback.onReturnString/
onRunResult` explicitamente). O `printer.service.ts` (frontend) dispara ~30 chamadas `await
IPosPrinter.*` em sequência. Como cada `await` resolve no **primeiro** disparo do callback (que
zera `call`), qualquer disparo tardio do callback cai no `PluginCall` da **próxima** operação,
resolvendo-a prematuramente ou deixando uma promise pendurada → trava/cras intermitente.

## Crítico

### `android/src/main/java/com/conecsa/iposprinter/IPosPrinterPlugin.java`
- [x] `IPosPrinterPlugin.java` — `JSObject r` e `PluginCall call` compartilhados. **✓ Corrigido**:
  removidos os campos compartilhados; cada `@PluginMethod` agora cria um callback dedicado via
  `resolver(call)` que usa um `JSObject` **novo** por resolução. API pública inalterada (o frontend
  só faz `await`). **(Crítico) [DECISÃO]** — *não exigiu mudança no frontend.*
- [x] `IPosPrinterPlugin.java` — garantir 1 resolução por `PluginCall`. **✓ Corrigido**: cada
  `resolver(call)` tem guarda `AtomicBoolean` (resolve no máximo 1×); como cada chamada tem seu
  próprio callback, um disparo tardio de uma operação anterior **não** resolve mais a chamada
  seguinte (raiz da quebra intermitente). Cobertura de regressão adicionada em `IPosPrinterTest`
  (overloads `getPrinterStatus`). **(Crítico) [DECISÃO]**

### `android/src/main/java/com/conecsa/iposprinter/IPosPrinter.java`
- [x] `IPosPrinter.java` — múltiplos disparos de callback por chamada JS (o serviço, o
  `printerPerformPrint` e a chamada explícita). **✓ Mitigado** sem alterar a lógica de impressão:
  os múltiplos disparos continuam existindo, mas agora são **idempotentes** graças à guarda
  `AtomicBoolean` por chamada no plugin — só o primeiro resolve. Adicionados overloads
  `getPrinterStatus(cb)` / `getPrinterStatus(int, cb)` (assinaturas antigas delegam, testes intactos).
  *Unificar para 1 disparo na origem fica como melhoria futura, opcional.* **(Crítico) [DECISÃO]**
- [ ] `IPosPrinter.java:137-175` — fila assíncrona sem timeout; se a chamada IPC nativa travar, a
  fila para. → **Adiado**: a correção propaga erro/`reject` ao JS, o que **exige** o frontend tratar
  rejeições (hoje não trata). Implementar junto da correção do frontend. **(Crítico) [DECISÃO]**
- [ ] `IPosPrinter.java:149-154` — `stopSelf()` ao esvaziar a fila. → **Adiado** (revisar ciclo de
  vida junto dos itens de fila acima). **(Crítico) [DECISÃO]**
- [ ] `IPosPrinter.java:101-123` (`bindService`) — falha de bind silenciosa; service desconectado
  faz os métodos retornarem sem disparar callback → promise pendura. → **Adiado**: a correção
  propaga `reject` ao JS, que **depende** do frontend tratar rejeições. **(Crítico)**

## Alto
- [ ] `IPosPrinter.java:222-228` (`getPrinterStatus`) existe e expõe os estados
  (`PRINTER_PAPERLESS`, `PRINTER_IS_BUSY`, etc., 29-34), mas **não é usado antes de imprimir**. →
  Expor/garantir verificação de status (papel/temperatura/busy) antes de enviar o lote de
  impressão, permitindo ao frontend abortar/reimprimir com mensagem clara. **(Alto)**
- [ ] `IPosPrinter.java:84-87,182-188` — `onServiceConnected` faz `printerInit` que pode lançar
  `RemoteException` re-empacotada em `RuntimeException` (86); inconsistência entre métodos que
  checam `isServiceUnconnected()` e os que não checam (ex.: `printBlankLines` 333, `printText` 347
  não checam). → Uniformizar a checagem de conexão em todos os métodos. **(Alto)**

## Médio / Qualidade
- [ ] `IPosPrinter.java` — logs `Log.i/d` por operação em produção (verbosos). → Reduzir nível de
  log no caminho quente de impressão. **(Qualidade)**
- [ ] Após estabilizar a API nativa, **regenerar o README** com `npm run build` (docgen) — o bloco
  de API do README é gerado a partir do JSDoc/source. **(Qualidade)**

## Verificação (quando implementar)
- `cd android && ./gradlew clean build test` (ou `npm run verify:android`).
- Teste manual no palm: imprimir um resultado completo (~30 chamadas) repetidas vezes e validar que
  (a) nenhuma promise pendura, (b) impressão é completa, (c) reimpressão funciona sem reabrir a app,
  (d) com impressora sem papel/ocupada a falha é reportada com mensagem.
