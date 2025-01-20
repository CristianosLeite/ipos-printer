package com.conecsa.ipos.printer.service;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.PluginCall;

@CapacitorPlugin(name = "IPosPrinter")
public class IPosPrinterPlugin extends Plugin {
  public IPosPrinter implementation;

  @Override
  public void load() {
      implementation = new IPosPrinter();
      implementation.load(getContext());
  }

  @PluginMethod
  public void print(PluginCall call) {
    JSObject r = new JSObject();

    String value = call.getString("value");
    if (value == null) {
      call.reject("Must provide a value");
      return;
    }

    r.put("value", IPosPrinterActivity.Printer.printText(value));
    call.resolve(r);
  }
}
