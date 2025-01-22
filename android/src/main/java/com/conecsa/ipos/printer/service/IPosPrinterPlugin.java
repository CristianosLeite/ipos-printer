package com.conecsa.ipos.printer.service;

import android.os.RemoteException;
import android.util.Log;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginCall;

@CapacitorPlugin(name = "IPosPrinter")
public class IPosPrinterPlugin extends Plugin {
  public IPosPrinter implementation;
  private IPosPrinterCallback callback = null;
  private final String TAG = "IPosPrinterPlugin";

  @Override
  public void load() {
      implementation = new IPosPrinter();
      implementation.bindService(getContext());
      callback = new IPosPrinterCallback.Stub() {

        @Override
        public void onRunResult(final boolean isSuccess) throws RemoteException {
          Log.i(TAG, "result:" + isSuccess + "\n");
        }

        @Override
        public void onReturnString(final String value) throws RemoteException {
          Log.i(TAG, "result:" + value + "\n");
        }
      };
  }

  @PluginMethod
  public void print(PluginCall call) throws RemoteException {
    JSObject r = new JSObject();

    String value = call.getString("value");
    if (value == null) {
      call.reject("Must provide a value");
      return;
    }

    implementation.mBinder.printText(value, callback);
    r.put("value", callback);
    call.resolve(r);
  }
}
