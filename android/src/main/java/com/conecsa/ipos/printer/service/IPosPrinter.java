package com.conecsa.ipos.printer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class IPosPrinter extends Service {
  public IPosPrinterActivity iPosPrinterActivity;

  public IPosPrinter() { }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }

  public void load(Context context) {
    iPosPrinterActivity = new IPosPrinterActivity(context);
  }
}