package com.conecsa.ipos.printer.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Future;

public class IPosPrinter extends Service {
  private final String TAG = "IPosPrinter";
  private IPosPrinterService mIPosPrinterService;
  private static final long TIMEOUT_SECONDS = 10;

  private void executeTransactWithTimeout(Runnable task) throws TimeoutException {
    Future<?> future = ThreadPoolManager.getInstance().submitTask(task);
    try {
      future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      future.cancel(true);
      throw new TimeoutException("Transact operation timed out");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

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
    mIPosPrinterService = IPosPrinterService.Stub.asInterface(mBinder);
    return mBinder;
  }

  private final ServiceConnection connectService = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      mIPosPrinterService = IPosPrinterService.Stub.asInterface(service);
      Log.i(TAG, "Service connected");
      Log.i(TAG, "binder: " + service);
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
      String packageName = context.getApplicationContext().getPackageName();
      String cls = "com.conecsa.ipos.printer.service.IPosPrinter";
      intent.setComponent(new ComponentName(packageName, cls));

      Log.d(TAG, "Attempting to bind to service with intent: " + intent.getPackage());

      context.startService(intent);
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

  public final IPosPrinterService.Stub mBinder = new IPosPrinterService.Stub() {
    @Override
    public void printerInit(IPosPrinterCallback callback) {
      android.os.Parcel _data = android.os.Parcel.obtain();
      android.os.Parcel _reply = android.os.Parcel.obtain();
      ThreadPoolManager.getInstance().executeTask(() -> {
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongInterface(callback);
//            try {
//                boolean _status = transact(Stub.TRANSACTION_printerInit, _data, _reply, 0);
//            } catch (RemoteException e) {
//                throw new RuntimeException(e);
//            }
            _reply.readException();
        }  finally {
          _reply.recycle();
          _data.recycle();
        }
      });
    }

    @Override
    public void printText(String text, IPosPrinterCallback callback) {
      android.os.Parcel _data = android.os.Parcel.obtain();
      android.os.Parcel _reply = android.os.Parcel.obtain();
      try {
        executeTransactWithTimeout(() -> {
          try {
            _data.writeInterfaceToken(DESCRIPTOR);
            _data.writeString(text);
            _data.writeStrongInterface(callback);
            //boolean _status = transact(Stub.TRANSACTION_printText, _data, _reply, 0);
              try {
                  callback.onRunResult(true);
              } catch (RemoteException e) {
                  throw new RuntimeException(e);
              }
              _reply.readException();
            Log.i(TAG, "printText: " + text);
//          } catch (RemoteException e) {
//            throw new RuntimeException(e);
          } finally {
            _reply.recycle();
            _data.recycle();
          }
        });
      } catch (TimeoutException e) {
        Log.e(TAG, "printText operation timed out", e);
      }
    }


    @Override
    public int getPrinterStatus() {
      android.os.Parcel _data = android.os.Parcel.obtain();
      android.os.Parcel _reply = android.os.Parcel.obtain();
      AtomicInteger _result = new AtomicInteger();
      try {
        executeTransactWithTimeout(() -> {
          try {
            _data.writeInterfaceToken(DESCRIPTOR);
            //boolean _status = transact(Stub.TRANSACTION_getPrinterStatus, _data, _reply, 0);
            _reply.readException();
            _result.set(_reply.readInt());
//          } catch (RemoteException e) {
//            throw new RuntimeException(e);
          } finally {
            _reply.recycle();
            _data.recycle();
          }
        });
      } catch (TimeoutException e) {
        Log.e(TAG, "getPrinterStatus operation timed out", e);
      }
      return _result.get();
    }

    @Override
    public void setPrinterPrintDepth(int depth, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void setPrinterPrintFontType(String typeface, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void setPrinterPrintFontSize(int fontsize, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void setPrinterPrintAlignment(int alignment, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printerFeedLines(int lines, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printBlankLines(int lines, int height, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printSpecifiedTypeText(String text, String typeface, int fontsize, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void PrintSpecFormatText(String text, String typeface, int fontsize, int alignment, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, int isContinuousPrint, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printBitmap(int alignment, int bitmapSize, Bitmap mBitmap, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printBarCode(String data, int symbology, int height, int width, int textposition, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printQRCode(String data, int modulesize, int mErrorCorrectionLevel, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printRawData(byte[] rawPrintData, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void sendUserCMDData(byte[] data, IPosPrinterCallback callback) throws RemoteException {

    }

    @Override
    public void printerPerformPrint(int feedlines, IPosPrinterCallback callback) {
      android.os.Parcel _data = android.os.Parcel.obtain();
      android.os.Parcel _reply = android.os.Parcel.obtain();
      ThreadPoolManager.getInstance().executeTask(() -> {
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeInt(feedlines);
          _data.writeStrongInterface(callback);
            try {
                boolean _status = transact(Stub.TRANSACTION_printerPerformPrint, _data, _reply, 0);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      });
    }

    @Override
    public IBinder asBinder() {
      return mBinder;
    }
  };
}