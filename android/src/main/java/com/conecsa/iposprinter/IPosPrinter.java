package com.conecsa.iposprinter;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.iposprinter.iposprinterservice.IPosPrinterCallback;
import com.iposprinter.iposprinterservice.IPosPrinterService;

public class IPosPrinter extends Service implements IPosPrinterService {
  private final String TAG = "IPosPrinter";
  private IPosPrinterService mIPosPrinterService;
  private IPosPrinterCallback callback;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Service created");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.i(TAG, "Service unbinding");
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mIPosPrinterService.asBinder();
  }

  public void setCallback(IPosPrinterCallback callback) {
    this.callback = callback;
  }

  private final ServiceConnection connectService = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
      Log.i(TAG, "Service connected");

        try {
            printerInit(callback);
            Log.i(TAG, "Printer initialized");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
      mIPosPrinterService = null;
      Log.i(TAG, "Service disconnected");
    }
  };

  public void bindService(Context context) {
    try {
      if (context == null) {
        Log.e(TAG, "Context is null, cannot bind to service");
        return;
      }

      Intent intent = new Intent();
      intent.setPackage("com.iposprinter.iposprinterservice");

      Log.d(TAG, "Attempting to bind to service with intent: " + intent.getPackage());

      boolean bound = context.bindService(intent, connectService, Context.BIND_AUTO_CREATE);
      if (!bound) {
        Log.e(TAG, "Failed to bind to service");
        return;
      }

      Log.i(TAG, "Service bound");
    } catch (Exception e) {
      Log.e(TAG, "Failed to bind to service", e);
    }
  }

  /**
   * Encapsulates the operation that throws RemoteException.
   * */
  @FunctionalInterface
  private interface AsyncPrinterOperation {
    void execute() throws RemoteException;
  }

  /**
   * Calls the interface inside the ThreadPoolManager.
   * @param operation
   */
  private void runAsyncPrinterOperation(AsyncPrinterOperation operation) {
    if (!isServiceConnected()) { return; }
    ThreadPoolManager.getInstance().executeTask(() -> {
      try {
        operation.execute();
      } catch (RemoteException e) {
        Log.e(TAG, "Falha na impressão", e);
      }
    });
  }

  /**
   * <h5>Check if proxy service is connected.
   * <p>Avoid null pointer reference.
   * @return boolean
   */
  private boolean isServiceConnected() {
    if (mIPosPrinterService == null) {
      Log.e(TAG, "mIPosPrinterService is null");
      return false;
    }
    return true;
  }

  @Override
  public int getPrinterStatus() throws RemoteException {
    if (! isServiceConnected()) { return 6; } // Return a status different from the printer default values
    return mIPosPrinterService.getPrinterStatus();
  }

  @Override
  public void printerInit(IPosPrinterCallback callback) throws RemoteException {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printerInit(callback);
      Log.i(TAG, "Printer initialized");
    });
  }

  @Override
  public void setPrinterPrintDepth(int depth, IPosPrinterCallback callback) {
    if (isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintDepth(depth, callback);
      Log.i(TAG, "Printer print depth set to: " + depth);
    });
  }

  @Override
  public void setPrinterPrintFontType(String typeface, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintFontType(typeface, callback);
      Log.i(TAG, "Printer print font type set to: " + typeface);
    });
  }

  @Override
  public void setPrinterPrintFontSize(int fontsize, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintFontSize(fontsize, callback);
      Log.i(TAG, "Printer print font size set to: " + fontsize);
    });
  }

  @Override
  public void setPrinterPrintAlignment(int alignment, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintAlignment(alignment, callback);
      Log.i(TAG, "Printer print alignment set to: " + alignment);
    });
  }

  @Override
  public void printerFeedLines(int lines, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printerFeedLines(lines, callback);
      Log.i(TAG, "Printer feed lines: " + lines);
    });
  }

  @Override
  public void printBlankLines(int lines, int height, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printBlankLines(lines, height, callback);
      Log.i(TAG, "Printed blank lines: " + lines);
    });
  }

  @Override
  public void printText(String text, IPosPrinterCallback callback) {
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printText(text, callback);
      mIPosPrinterService.printerPerformPrint(160, callback);
      Log.i(TAG, "Sent text to printer");
      Log.i(TAG, "Text: " + text);
    });
  }

  @Override
  public void printSpecifiedTypeText(String text, String typeface, int fontsize, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printSpecifiedTypeText(text, typeface, fontsize, callback);
      Log.i(TAG, "Sent specified type text to printer");
      Log.i(TAG, "Text: " + text);
    });
  }

  @Override
  public void PrintSpecFormatText(String text, String typeface, int fontsize, int alignment, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.PrintSpecFormatText(text, typeface, fontsize, alignment, callback);
      Log.i(TAG, "Sent specified format text to printer");
      Log.i(TAG, "Text: " + text);
    });
  }

  @Override
  public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, int isContinuousPrint, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printColumnsText(colsTextArr, colsWidthArr, colsAlign, isContinuousPrint, callback);
      Log.i(TAG, "Sent columns text to printer");
    });
  }

  @Override
  public void printBitmap(int alignment, int bitmapSize, Bitmap mBitmap, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printBitmap(alignment, bitmapSize, mBitmap, callback);
      Log.i(TAG, "Sent bitmap to printer");
    });
  }

  @Override
  public void printBarCode(String data, int symbology, int height, int width, int textposition, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printBarCode(data, symbology, height, width, textposition, callback);
      Log.i(TAG, "Sent barcode to printer");
    });
  }

  @Override
  public void printQRCode(String data, int modulesize, int mErrorCorrectionLevel, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printQRCode(data, modulesize, mErrorCorrectionLevel, callback);
      Log.i(TAG, "Sent QR code to printer");
    });
  }

  @Override
  public void printRawData(byte[] rawPrintData, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printRawData(rawPrintData, callback);
      Log.i(TAG, "Sent raw data to printer");
    });
  }

  @Override
  public void sendUserCMDData(byte[] data, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.sendUserCMDData(data, callback);
      Log.i(TAG, "Sent user command data to printer");
    });
  }

  @Override
  public void printerPerformPrint(int feedlines, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printerPerformPrint(feedlines, callback);
      Log.i(TAG, "Performed print");
    });
  }

  @Override
  public IBinder asBinder() {
    return null;
  }
}