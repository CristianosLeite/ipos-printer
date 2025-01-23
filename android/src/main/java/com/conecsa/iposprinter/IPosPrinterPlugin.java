package com.conecsa.iposprinter;

import android.os.RemoteException;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginCall;
import com.iposprinter.iposprinterservice.IPosPrinterCallback;

@CapacitorPlugin(name = "IPosPrinter")
public class IPosPrinterPlugin extends Plugin {
  public IPosPrinter implementation;
  private IPosPrinterCallback callback = null;
  private final String TAG = "IPosPrinterPlugin";

  @Override
  public void load() {
      implementation = new IPosPrinter();
      implementation.bindService(getContext());
      // Callback must be declared here so it can be passed as parameter to the service methods
      callback = new IPosPrinterCallback.Stub() {

        @Override
        public void onRunResult(final boolean isSuccess) throws RemoteException {
          Log.i(TAG, "result:" + isSuccess);
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
          Log.i(TAG, "result:" + value);
        }
      };
      // Used to initialize the printer
      implementation.setCallback(callback);
  }

  @PluginMethod
  public void print(PluginCall call) throws RemoteException {
    JSObject r = new JSObject();

    String value = call.getString("value");
    if (value == null) {
      call.reject("Must provide a value");
      return;
    }

    implementation.printText(value, callback);
    r.put("value", callback);
    call.resolve(r);
  }
}
