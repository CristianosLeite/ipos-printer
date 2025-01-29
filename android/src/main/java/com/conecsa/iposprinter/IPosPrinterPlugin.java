package com.conecsa.iposprinter;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginCall;
import com.iposprinter.iposprinterservice.IPosPrinterCallback;

import java.util.Objects;

import com.conecsa.iposprinter.Utils.BitmapHandler;

@CapacitorPlugin(name = "IPosPrinter")
public class IPosPrinterPlugin extends Plugin {
  private final String TAG = "IPosPrinterPlugin";
  private final JSObject r = new JSObject();
  private PluginCall call;

  private IPosPrinter implementation;
  private IPosPrinterCallback callback = null;

  @Override
  public void load() {
    implementation = new IPosPrinter();
    implementation.bindService(getContext());

    // Callback must be declared here so it can be passed as parameter to the service methods
    callback = new IPosPrinterCallback.Stub() {

      @Override
      public void onRunResult(final boolean isSuccess) throws RemoteException {
        if (call == null) { return; }
        Log.i(TAG, "result:" + isSuccess);
        r.put("value", isSuccess);
        call.resolve(r);
        call = null;
      }

      @Override
      public void onReturnString(final String value) throws RemoteException {
        if (call == null) { return; }
        Log.i(TAG, "result:" + value);
        r.put("value", value);
        call.resolve(r);
        call = null;
      }
    };

    // Used to initialize the printer
    implementation.setCallback(callback);
  }

  @PluginMethod
  public void getPrinterStatus(PluginCall call) throws RemoteException {
    this.call = call;
    implementation.getPrinterStatus();
  }

  @PluginMethod
  public void getPrinterStatusMessage(PluginCall call) throws RemoteException {
    this.call = call;
    Integer status = call.getInt("status");
    if (status == null) {
      call.reject("Must provide a status value");
      return;
    }
    implementation.getPrinterStatus(status);
  }

  @PluginMethod
  public void setPrinterPrintDepth(PluginCall call) throws RemoteException {
    this.call = call;
    Integer value = call.getInt("depth");
    if (value == null) {
      call.reject("Must provide a depth value");
    }
    assert value != null;
    implementation.setPrinterPrintDepth(value, callback);
  }

  @PluginMethod
  public void setPrinterPrintFontType(PluginCall call) throws RemoteException {
    this.call = call;
    String value = call.getString("typeface");
    if (value == null) {
      call.reject("Must provide a typeface");
    }
    implementation.setPrinterPrintFontType(value, callback);
  }

  @PluginMethod
  public void setPrinterPrintFontSize(PluginCall call) throws RemoteException {
    this.call = call;
    Integer value = call.getInt("fontSize");
    if (value == null) {
      call.reject("Must provide a fontSize value");
    }
    assert value != null;
    implementation.setPrinterPrintFontSize(value, callback);
  }

  @PluginMethod
  public void setPrinterPrintAlignment(PluginCall call) throws RemoteException {
    this.call = call;
    Integer value = call.getInt("alignment");
    if (value == null) {
      call.reject("Must provide an alignment value");
    }
    assert value != null;
    implementation.setPrinterPrintAlignment(value, callback);
  }

  @PluginMethod
  public void printerFeedLines(PluginCall call) throws RemoteException {
    this.call = call;
    String value = call.getString("lines");
    if (value == null) {
      call.reject("Must provide a lines value");
    }
    assert value != null;
    implementation.printerFeedLines(Integer.parseInt(value), callback);
  }

  @PluginMethod
  public void printBlankLines(PluginCall call) throws RemoteException {
    this.call = call;
    String lines = call.getString("lines");
    String height = call.getString("height");
    if (lines == null || height == null) {
      call.reject("Must provide a lines and height value");
    }
    assert lines != null;
    assert height != null;
    implementation.printBlankLines(Integer.parseInt(lines), Integer.parseInt(height), callback);
  }

  @PluginMethod
  public void printText(PluginCall call) throws RemoteException {
    this.call = call;
    String text = call.getString("text");
    if (text == null) {
      call.reject("Must provide a text");
      return;
    }
    implementation.printText(text, callback);
  }

  @PluginMethod
  public void printSpecifiedTypeText(PluginCall call) throws RemoteException {
    this.call = call;
    String text = call.getString("text");
    String typeface = call.getString("typeface");
    String fontSize = call.getString("fontSize");
    if (text == null || typeface == null || fontSize == null) {
      call.reject("Must provide a text, typeface and fontSize");
      return;
    }
    implementation.printSpecifiedTypeText(text, typeface, Integer.parseInt(fontSize), callback);
  }

  @PluginMethod
  public void PrintSpecFormatText(PluginCall call) throws RemoteException {
    this.call = call;
    String text = call.getString("text");
    String typeface = call.getString("typeface");
    String fontSize = call.getString("fontSize");
    String alignment = call.getString("alignment");
    if (text == null || typeface == null || fontSize == null || alignment == null) {
      call.reject("Must provide a text, typeface, fontSize and alignment");
      return;
    }
    implementation.PrintSpecFormatText(text, typeface, Integer.parseInt(fontSize), Integer.parseInt(alignment), callback);
  }

  @PluginMethod
  public void printColumnsText(PluginCall call) throws RemoteException {
    this.call = call;
    String[] colsTextArr = new String[]{call.getString("colsTextArr")};
    int[] colsWidthArr = new int[]{Integer.parseInt(Objects.requireNonNull(call.getString("colsWidthArr")))};
    int[] colsAlign = new int[]{Integer.parseInt(Objects.requireNonNull(call.getString("colsAlign")))};
    String isContinuousPrint = call.getString("isContinuousPrint");
    if (isContinuousPrint == null) {
      call.reject("Must provide a colsTextArr, colsWidthArr, colsAlign and isContinuousPrint");
      return;
    }
    implementation.printColumnsText(colsTextArr, colsWidthArr, colsAlign, Integer.parseInt(isContinuousPrint), callback);
  }

  @PluginMethod
  public void printBitmap(PluginCall call) throws RemoteException {
    this.call = call;
    String alignment = call.getString("alignment");
    String bitmapSize = call.getString("bitmapSize");
    Bitmap bitmap = BitmapHandler.convertFromBase64(call.getString("bitmap"));
    if (alignment == null || bitmapSize == null) {
      call.reject("Must provide an alignment, bitmapSize and bitmap");
      return;
    }
    implementation.printBitmap(Integer.parseInt(alignment), Integer.parseInt(bitmapSize), bitmap, callback);
  }

  @PluginMethod
  public void printBarCode(PluginCall call) throws RemoteException {
    this.call = call;
    String data = call.getString("data");
    String symbology = call.getString("symbology");
    String height = call.getString("height");
    String width = call.getString("width");
    String textPosition = call.getString("textPosition");
    if (data == null || symbology == null || height == null || width == null || textPosition == null) {
      call.reject("Must provide a data, symbology, height, width and textPosition");
      return;
    }
    implementation.printBarCode(data, Integer.parseInt(symbology), Integer.parseInt(height), Integer.parseInt(width), Integer.parseInt(textPosition), callback);
  }

  @PluginMethod
  public void printQRCode(PluginCall call) throws RemoteException {
    this.call = call;
    String data = call.getString("data");
    String moduleSize = call.getString("moduleSize");
    String mErrorCorrectionLevel = call.getString("mErrorCorrectionLevel");
    if (data == null || moduleSize == null || mErrorCorrectionLevel == null) {
      call.reject("Mus provide a data, moduleSize and mErrorCorrectionLevel");
      return;
    }
    implementation.printQRCode(data, Integer.parseInt(moduleSize), Integer.parseInt(mErrorCorrectionLevel), callback);
  }

  @PluginMethod
  public void printRawData(PluginCall call) throws RemoteException {
    this.call = call;
    String rawPrintData = call.getString("data");
    if (rawPrintData == null) {
      call.reject("Must provide a data");
      return;
    }
    implementation.printRawData(rawPrintData.getBytes(), callback);
  }

  @PluginMethod
  public void printRowBlock(PluginCall call) {
    this.call = call;
    implementation.printRowBlock(callback);
  }
}
