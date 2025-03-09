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

import com.conecsa.iposprinter.Utils.BitmapHandler;

import org.json.JSONException;

@CapacitorPlugin(name = "IPosPrinter")
public class IPosPrinterPlugin extends Plugin {
  private final String TAG = "IPosPrinterPlugin";
  private final JSObject r = new JSObject();
  private PluginCall call;

  private IPosPrinter implementation;
  private IPosPrinterCallback callback;

  @Override
  public void load() {
    implementation = new IPosPrinter();
    implementation.bindService(getContext());

    // Callback must be declared here so it can be passed as parameter to the service methods
    callback = setCallback();

    // Used to initialize the printer
    implementation.setCallback(callback);
  }

  @Override
  public void handleOnDestroy() {
    super.handleOnDestroy();
    implementation.onDestroy();
  }

  private IPosPrinterCallback setCallback() {
    return new IPosPrinterCallback.Stub() {

      @Override
      public void onRunResult(final boolean isSuccess) {
        if (call == null) { return; }
        Log.i(TAG, "result:" + isSuccess);
        r.put("value", isSuccess);
        call.resolve(r);
        call = null;
      }

      @Override
      public void onReturnString(final String value) {
        if (call == null) { return; }
        Log.i(TAG, "result:" + value);
        r.put("value", value);
        call.resolve(r);
        call = null;
      }
    };
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
  public void setPrinterPrintDepth(PluginCall call) {
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
  public void setPrinterPrintFontSize(PluginCall call) {
    this.call = call;
    Integer value = call.getInt("fontSize");
    if (value == null) {
      call.reject("Must provide a fontSize value");
    }
    assert value != null;
    implementation.setPrinterPrintFontSize(value, callback);
  }

  @PluginMethod
  public void setPrinterPrintAlignment(PluginCall call) {
    this.call = call;
    Integer value = call.getInt("alignment");
    if (value == null) {
      call.reject("Must provide an alignment value");
    }
    assert value != null;
    implementation.setPrinterPrintAlignment(value, callback);
  }

  @PluginMethod
  public void printBlankLines(PluginCall call) {
    this.call = call;
    Integer lines = call.getInt("lines");
    Integer height = call.getInt("height");
    if (lines == null || height == null) {
      call.reject("Must provide a lines and height value");
    }
    assert lines != null;
    assert height != null;
    implementation.printBlankLines(lines, height, callback);
  }

  @PluginMethod
  public void printText(PluginCall call) {
    this.call = call;
    String text = call.getString("text");
    if (text == null) {
      call.reject("Must provide a text");
      return;
    }
    implementation.printText(text, callback);
  }

  @PluginMethod
  public void printSpecifiedTypeText(PluginCall call) {
    this.call = call;
    String text = call.getString("text");
    String typeface = call.getString("typeface");
    Integer fontSize = call.getInt("fontSize");
    if (text == null || typeface == null || fontSize == null) {
      call.reject("Must provide a text, typeface and fontSize");
      return;
    }
    implementation.printSpecifiedTypeText(text, typeface, fontSize, callback);
  }

  @PluginMethod
  public void PrintSpecFormatText(PluginCall call) {
    this.call = call;
    String text = call.getString("text");
    String typeface = call.getString("typeface");
    Integer fontSize = call.getInt("fontSize");
    Integer alignment = call.getInt("alignment");
    if (text == null || typeface == null || fontSize == null || alignment == null) {
      call.reject("Must provide a text, typeface, fontSize and alignment");
      return;
    }
    implementation.PrintSpecFormatText(text, typeface, fontSize, alignment, callback);
  }

  @PluginMethod
  public void printColumnsText(PluginCall call) throws JSONException {
    this.call = call;

    String[] colsTextArr = call.getArray("colsTextArr").toList().stream()
            .map(Object::toString)
            .toArray(String[]::new);

    int[] colsWidthArr = call.getArray("colsWidthArr").toList().stream()
            .mapToInt(obj -> Integer.parseInt(obj.toString()))
            .toArray();

    int[] colsAlignArr = call.getArray("colsAlignArr").toList().stream()
            .mapToInt(obj -> Integer.parseInt(obj.toString()))
            .toArray();

    Integer isContinuousPrint = call.getInt("isContinuousPrint");
    if (colsTextArr.length == 0 || colsWidthArr.length == 0 || colsAlignArr.length == 0 || isContinuousPrint == null) {
      call.reject("Must provide a cols text array, cols width array, cols align array and if is a continuous print");
      return;
    }

    implementation.printColumnsText(colsTextArr, colsWidthArr, colsAlignArr, isContinuousPrint, callback);
  }

  @PluginMethod
  public void printBitmap(PluginCall call) {
    this.call = call;
    Integer alignment = call.getInt("alignment");
    Integer bitmapSize = call.getInt("bitmapSize");
    String base64 = call.getString("base64");
    if (alignment == null || bitmapSize == null || base64 == null) {
      call.reject("Must provide an alignment, bitmapSize and bitmap");
      return;
    }
    Bitmap bitmap = BitmapHandler.convertFromBase64(base64);
    implementation.printBitmap(alignment, bitmapSize, bitmap, callback);
  }

  @PluginMethod
  public void printBarCode(PluginCall call) {
    this.call = call;
    String data = call.getString("data");
    Integer symbology = call.getInt("symbology");
    Integer height = call.getInt("height");
    Integer width = call.getInt("width");
    Integer textPosition = call.getInt("textPosition");
    if (data == null || symbology == null || height == null || width == null || textPosition == null) {
      call.reject("Must provide a data, symbology, height, width and textPosition");
      return;
    }
    implementation.printBarCode(data, symbology, height, width, textPosition, callback);
  }

  @PluginMethod
  public void printQRCode(PluginCall call) {
    this.call = call;
    String data = call.getString("data");
    Integer moduleSize = call.getInt("moduleSize");
    Integer mErrorCorrectionLevel = call.getInt("errorCorrectionLevel");
    if (data == null || moduleSize == null || mErrorCorrectionLevel == null) {
      call.reject("Mus provide a data, module size and the error correction level");
      return;
    }
    implementation.printQRCode(data, moduleSize, mErrorCorrectionLevel, callback);
  }

  @PluginMethod
  public void printRawData(PluginCall call) {
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
