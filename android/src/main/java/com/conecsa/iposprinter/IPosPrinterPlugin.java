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

  private IPosPrinter implementation;

  @Override
  public void load() {
    implementation = new IPosPrinter();
    implementation.bindService(getContext());

    // Connection-time callback used only by printerInit during the ServiceConnection. It is NOT
    // tied to any PluginCall, so it must never resolve one - it only logs.
    implementation.setCallback(new IPosPrinterCallback.Stub() {
      @Override
      public void onRunResult(final boolean isSuccess) {
        Log.i(TAG, "init result: " + isSuccess);
      }

      @Override
      public void onReturnString(final String value) {
        Log.i(TAG, "init result: " + value);
      }
    });
  }

  @Override
  public void handleOnDestroy() {
    super.handleOnDestroy();
    implementation.onDestroy();
  }

  /**
   * Builds a callback bound to a single {@link PluginCall}.
   *
   * <p>The native printer service may invoke the callback more than once per operation (the
   * service itself is handed the callback, {@code printerPerformPrint} is handed it again, and the
   * wrapper also calls it explicitly). The {@link SingleShotGuard} ensures the call is resolved
   * exactly once. Crucially, because every {@code PluginCall} gets its own callback instance, a
   * late firing left over from a previous operation can no longer resolve a subsequent operation's
   * call - which was the source of the intermittent printing crash/hang.
   *
   * @param call The Capacitor call to resolve once.
   * @return A fresh, single-shot callback bound to {@code call}.
   */
  private IPosPrinterCallback resolver(final PluginCall call) {
    return new IPosPrinterCallback.Stub() {
      private final SingleShotGuard guard = new SingleShotGuard();

      @Override
      public void onRunResult(final boolean isSuccess) {
        if (!guard.tryResolve()) { return; }
        Log.i(TAG, "result:" + isSuccess);
        JSObject ret = new JSObject();
        ret.put("value", isSuccess);
        call.resolve(ret);
      }

      @Override
      public void onReturnString(final String value) {
        if (!guard.tryResolve()) { return; }
        Log.i(TAG, "result:" + value);
        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
      }
    };
  }

  @PluginMethod
  public void getPrinterStatus(PluginCall call) throws RemoteException {
    implementation.getPrinterStatus(resolver(call));
  }

  @PluginMethod
  public void getPrinterStatusMessage(PluginCall call) throws RemoteException {
    Integer status = call.getInt("status");
    if (status == null) {
      call.reject("Must provide a status value");
      return;
    }
    implementation.getPrinterStatus(status, resolver(call));
  }

  @PluginMethod
  public void setPrinterPrintDepth(PluginCall call) {
    Integer value = call.getInt("depth");
    if (value == null) {
      call.reject("Must provide a depth value");
      return;
    }
    implementation.setPrinterPrintDepth(value, resolver(call));
  }

  @PluginMethod
  public void setPrinterPrintFontType(PluginCall call) throws RemoteException {
    String value = call.getString("typeface");
    if (value == null) {
      call.reject("Must provide a typeface");
      return;
    }
    implementation.setPrinterPrintFontType(value, resolver(call));
  }

  @PluginMethod
  public void setPrinterPrintFontSize(PluginCall call) {
    Integer value = call.getInt("fontSize");
    if (value == null) {
      call.reject("Must provide a fontSize value");
      return;
    }
    implementation.setPrinterPrintFontSize(value, resolver(call));
  }

  @PluginMethod
  public void setPrinterPrintAlignment(PluginCall call) {
    Integer value = call.getInt("alignment");
    if (value == null) {
      call.reject("Must provide an alignment value");
      return;
    }
    implementation.setPrinterPrintAlignment(value, resolver(call));
  }

  @PluginMethod
  public void printBlankLines(PluginCall call) {
    Integer lines = call.getInt("lines");
    Integer height = call.getInt("height");
    if (lines == null || height == null) {
      call.reject("Must provide a lines and height value");
      return;
    }
    implementation.printBlankLines(lines, height, resolver(call));
  }

  @PluginMethod
  public void printText(PluginCall call) {
    String text = call.getString("text");
    if (text == null) {
      call.reject("Must provide a text");
      return;
    }
    implementation.printText(text, resolver(call));
  }

  @PluginMethod
  public void printSpecifiedTypeText(PluginCall call) {
    String text = call.getString("text");
    String typeface = call.getString("typeface");
    Integer fontSize = call.getInt("fontSize");
    if (text == null || typeface == null || fontSize == null) {
      call.reject("Must provide a text, typeface and fontSize");
      return;
    }
    implementation.printSpecifiedTypeText(text, typeface, fontSize, resolver(call));
  }

  @PluginMethod
  public void PrintSpecFormatText(PluginCall call) {
    String text = call.getString("text");
    String typeface = call.getString("typeface");
    Integer fontSize = call.getInt("fontSize");
    Integer alignment = call.getInt("alignment");
    if (text == null || typeface == null || fontSize == null || alignment == null) {
      call.reject("Must provide a text, typeface, fontSize and alignment");
      return;
    }
    implementation.PrintSpecFormatText(text, typeface, fontSize, alignment, resolver(call));
  }

  @PluginMethod
  public void printColumnsText(PluginCall call) throws JSONException {
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

    implementation.printColumnsText(colsTextArr, colsWidthArr, colsAlignArr, isContinuousPrint, resolver(call));
  }

  @PluginMethod
  public void printBitmap(PluginCall call) {
    Integer alignment = call.getInt("alignment");
    Integer bitmapSize = call.getInt("bitmapSize");
    String base64 = call.getString("base64");
    if (alignment == null || bitmapSize == null || base64 == null) {
      call.reject("Must provide an alignment, bitmapSize and bitmap");
      return;
    }
    Bitmap bitmap = BitmapHandler.convertFromBase64(base64);
    implementation.printBitmap(alignment, bitmapSize, bitmap, resolver(call));
  }

  @PluginMethod
  public void printBarCode(PluginCall call) {
    String data = call.getString("data");
    Integer symbology = call.getInt("symbology");
    Integer height = call.getInt("height");
    Integer width = call.getInt("width");
    Integer textPosition = call.getInt("textPosition");
    if (data == null || symbology == null || height == null || width == null || textPosition == null) {
      call.reject("Must provide a data, symbology, height, width and textPosition");
      return;
    }
    implementation.printBarCode(data, symbology, height, width, textPosition, resolver(call));
  }

  @PluginMethod
  public void printQRCode(PluginCall call) {
    String data = call.getString("data");
    Integer moduleSize = call.getInt("moduleSize");
    Integer mErrorCorrectionLevel = call.getInt("errorCorrectionLevel");
    if (data == null || moduleSize == null || mErrorCorrectionLevel == null) {
      call.reject("Mus provide a data, module size and the error correction level");
      return;
    }
    implementation.printQRCode(data, moduleSize, mErrorCorrectionLevel, resolver(call));
  }

  @PluginMethod
  public void printRawData(PluginCall call) {
    String rawPrintData = call.getString("data");
    if (rawPrintData == null) {
      call.reject("Must provide a data");
      return;
    }
    implementation.printRawData(rawPrintData.getBytes(), resolver(call));
  }

  @PluginMethod
  public void printRowBlock(PluginCall call) {
    implementation.printRowBlock(resolver(call));
  }
}
