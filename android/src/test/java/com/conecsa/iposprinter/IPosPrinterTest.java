package com.conecsa.iposprinter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

/**
 * Local unit tests for {@link IPosPrinter}.
 *
 * <p>{@code IPosPrinter} extends {@code android.app.Service} and delegates to a bound
 * {@code IPosPrinterService}. These tests inject a mock service via reflection (the field is
 * private and normally set by the {@code ServiceConnection}) and verify the wrapper's behavior:
 * the unconnected guards, input validation, status mapping, and the asynchronous delegation /
 * callback contract. Asynchronous operations run on {@link ThreadPoolManager}, so delegation is
 * asserted with Mockito's {@code timeout()}.
 *
 * <p>Calls into the Android framework (e.g. {@code Log}, {@code Service#stopSelf}) are no-ops here
 * thanks to {@code testOptions.unitTests.returnDefaultValues = true}.
 */
public class IPosPrinterTest {

  private static final long ASYNC_TIMEOUT_MS = 2000;

  private IPosPrinter printer;

  @Mock private IPosPrinterService service;
  @Mock private IPosPrinterCallback callback;
  @Mock private IPosPrinterCallback perCallCallback;

  @Before
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    printer = new IPosPrinter();
    printer.setCallback(callback);
  }

  /** Inject a (mock) service into the private {@code mIPosPrinterService} field. */
  private void connectService(IPosPrinterService injected) throws Exception {
    Field field = IPosPrinter.class.getDeclaredField("mIPosPrinterService");
    field.setAccessible(true);
    field.set(printer, injected);
  }

  // --- getPrinterStatus(int) status-to-message mapping ---------------------------------------

  @Test
  public void getPrinterStatus_mapsNormal() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_NORMAL);
    verify(callback).onReturnString("Printer normal");
  }

  @Test
  public void getPrinterStatus_mapsPaperless() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_PAPERLESS);
    verify(callback).onReturnString("Printer paperless");
  }

  @Test
  public void getPrinterStatus_mapsThpHighTemperature() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_THP_HIGH_TEMPERATURE);
    verify(callback).onReturnString("Printer THP high temperature");
  }

  @Test
  public void getPrinterStatus_mapsMotorHighTemperature() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_MOTOR_HIGH_TEMPERATURE);
    verify(callback).onReturnString("Printer motor high temperature");
  }

  @Test
  public void getPrinterStatus_mapsBusy() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_IS_BUSY);
    verify(callback).onReturnString("Printer is busy");
  }

  @Test
  public void getPrinterStatus_mapsErrorUnknown() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_ERROR_UNKNOWN);
    verify(callback).onReturnString("Printer error unknown");
  }

  @Test
  public void getPrinterStatus_mapsUnknownValueToDefault() throws Exception {
    printer.getPrinterStatus(999);
    verify(callback).onReturnString("Printer status unknown");
  }

  // --- getPrinterStatus() --------------------------------------------------------------------

  @Test
  public void getPrinterStatus_returnsSentinelWhenUnconnected() throws Exception {
    int status = printer.getPrinterStatus();
    assertEquals(6, status);
    verify(callback, never()).onReturnString(any());
  }

  @Test
  public void getPrinterStatus_returnsServiceValueWhenConnected() throws Exception {
    connectService(service);
    when(service.getPrinterStatus()).thenReturn(printer.PRINTER_IS_BUSY);

    int status = printer.getPrinterStatus();

    assertEquals(printer.PRINTER_IS_BUSY, status);
    verify(callback).onReturnString("4");
  }

  // --- per-call callback overloads (race-condition fix) --------------------------------------

  @Test
  public void getPrinterStatusMessage_withPerCallCallback_usesSuppliedCallbackOnly() throws Exception {
    printer.getPrinterStatus(printer.PRINTER_PAPERLESS, perCallCallback);
    verify(perCallCallback).onReturnString("Printer paperless");
    // The shared/connection-time callback must not be touched by a per-call request.
    verify(callback, never()).onReturnString(any());
  }

  @Test
  public void getPrinterStatus_withPerCallCallback_returnsSentinelWhenUnconnected() throws Exception {
    int status = printer.getPrinterStatus(perCallCallback);
    assertEquals(6, status);
    verify(perCallCallback, never()).onReturnString(any());
    verify(callback, never()).onReturnString(any());
  }

  @Test
  public void getPrinterStatus_withPerCallCallback_usesSuppliedCallbackOnly() throws Exception {
    connectService(service);
    when(service.getPrinterStatus()).thenReturn(printer.PRINTER_IS_BUSY);

    int status = printer.getPrinterStatus(perCallCallback);

    assertEquals(printer.PRINTER_IS_BUSY, status);
    verify(perCallCallback).onReturnString("4");
    verify(callback, never()).onReturnString(any());
  }

  // --- printerInit ---------------------------------------------------------------------------

  @Test
  public void printerInit_skippedWhenUnconnected() throws Exception {
    printer.printerInit(callback);
    verify(callback, never()).onReturnString(any());
  }

  @Test
  public void printerInit_delegatesAndReportsWhenConnected() throws Exception {
    connectService(service);
    printer.printerInit(callback);
    verify(service).printerInit(callback);
    verify(callback).onReturnString("Printer initialized");
  }

  // --- setPrinterPrintFontType validation ----------------------------------------------------

  @Test
  public void setPrinterPrintFontType_rejectsUnsupportedTypeface() throws Exception {
    connectService(service);
    printer.setPrinterPrintFontType("Arial", callback);
    verify(callback).onReturnString("Typeface is not supported");
    verify(service, never()).setPrinterPrintFontType(any(), any());
  }

  @Test
  public void setPrinterPrintFontType_acceptsSt() throws Exception {
    connectService(service);
    printer.setPrinterPrintFontType("ST", callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).setPrinterPrintFontType("ST", callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Typeface set to: ST");
  }

  @Test
  public void setPrinterPrintFontType_skippedWhenUnconnected() throws Exception {
    printer.setPrinterPrintFontType("ST", callback);
    verify(callback, never()).onReturnString(any());
  }

  // --- setter delegation ---------------------------------------------------------------------

  @Test
  public void setPrinterPrintDepth_delegatesAsync() throws Exception {
    connectService(service);
    printer.setPrinterPrintDepth(6, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).setPrinterPrintDepth(6, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Printer depth set to: 6");
  }

  @Test
  public void setPrinterPrintDepth_skippedWhenUnconnected() throws Exception {
    printer.setPrinterPrintDepth(6, callback);
    verify(callback, never()).onReturnString(any());
  }

  @Test
  public void setPrinterPrintFontSize_delegatesAsync() throws Exception {
    connectService(service);
    printer.setPrinterPrintFontSize(24, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).setPrinterPrintFontSize(24, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Font size set to: 24");
  }

  @Test
  public void setPrinterPrintAlignment_reportsLeft() throws Exception {
    connectService(service);
    printer.setPrinterPrintAlignment(0, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).setPrinterPrintAlignment(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Alignment set to: Left");
  }

  @Test
  public void setPrinterPrintAlignment_reportsRight() throws Exception {
    connectService(service);
    printer.setPrinterPrintAlignment(2, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Alignment set to: Right");
  }

  @Test
  public void setPrinterPrintAlignment_reportsCenterForOtherValues() throws Exception {
    connectService(service);
    printer.setPrinterPrintAlignment(1, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Alignment set to: Center");
  }

  // --- print operations ----------------------------------------------------------------------

  @Test
  public void printText_delegatesPerformsPrintAndReportsSuccess() throws Exception {
    connectService(service);
    printer.printText("hello", callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printText("hello", callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onRunResult(true);
  }

  @Test
  public void printSpecifiedTypeText_delegatesAndPerformsPrint() throws Exception {
    connectService(service);
    printer.printSpecifiedTypeText("hi", "ST", 24, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printSpecifiedTypeText("hi", "ST", 24, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS))
        .onReturnString("Text: hi Typeface: ST Font size: 24");
  }

  @Test
  public void printSpecFormatText_delegatesAndPerformsPrint() throws Exception {
    connectService(service);
    printer.PrintSpecFormatText("hi", "ST", 24, 1, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).PrintSpecFormatText("hi", "ST", 24, 1, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS))
        .onReturnString("Text: hi Typeface: ST Font size: 24 Alignment: 1");
  }

  @Test
  public void printColumnsText_delegatesAndPerformsPrint() throws Exception {
    connectService(service);
    String[] cols = {"a", "b"};
    int[] widths = {10, 10};
    int[] aligns = {0, 2};
    printer.printColumnsText(cols, widths, aligns, 0, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printColumnsText(cols, widths, aligns, 0, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
  }

  @Test
  public void printBarCode_delegatesAndReports() throws Exception {
    connectService(service);
    printer.printBarCode("12345", 8, 6, 12, 2, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printBarCode("12345", 8, 6, 12, 2, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Code: 12345 Symbology: 8");
  }

  @Test
  public void printBarCode_skippedWhenUnconnected() throws Exception {
    printer.printBarCode("12345", 8, 6, 12, 2, callback);
    verify(callback, never()).onReturnString(any());
  }

  @Test
  public void printQRCode_delegatesAndReports() throws Exception {
    connectService(service);
    printer.printQRCode("payload", 10, 3, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printQRCode("payload", 10, 3, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("QR code printed. Code: payload");
  }

  @Test
  public void printRawData_delegatesAndPerformsPrint() throws Exception {
    connectService(service);
    byte[] data = {0x01, 0x02};
    printer.printRawData(data, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printRawData(data, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
  }

  @Test
  public void sendUserCMDData_delegatesWithoutPerformingPrint() throws Exception {
    connectService(service);
    byte[] data = {0x1B, 0x40};
    printer.sendUserCMDData(data, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).sendUserCMDData(data, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("User command data printed");
    verify(service, never()).printerPerformPrint(anyInt(), any());
  }

  @Test
  public void printerPerformPrint_delegatesAsync() throws Exception {
    connectService(service);
    printer.printerPerformPrint(3, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(3, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Performed print");
  }

  @Test
  public void printBlankLines_delegatesAsync() throws Exception {
    connectService(service);
    printer.printBlankLines(2, 24, callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printBlankLines(2, 24, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Blank lines: 2 lines");
  }

  @Test
  public void printRowBlock_delegatesRawDataAndPerformsPrint() throws Exception {
    connectService(service);
    printer.printRowBlock(callback);
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printRawData(any(byte[].class), eq(callback));
    verify(service, timeout(ASYNC_TIMEOUT_MS)).printerPerformPrint(0, callback);
    verify(callback, timeout(ASYNC_TIMEOUT_MS)).onReturnString("Block line printed");
  }

  @Test
  public void printRowBlock_skippedWhenUnconnected() throws Exception {
    printer.printRowBlock(callback);
    verify(callback, never()).onReturnString(any());
  }
}
