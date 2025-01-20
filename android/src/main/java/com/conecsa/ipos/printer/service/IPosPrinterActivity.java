package com.conecsa.ipos.printer.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.lang.reflect.Proxy;
import java.util.concurrent.CountDownLatch;

public class IPosPrinterActivity  {
    @SuppressLint("StaticFieldLeak")
    public static Context context;

    public IPosPrinterActivity(Context context) {
        IPosPrinterActivity.context = context;
        Printer.bindService();
    }

    public static class Printer extends Activity {
        private static final String TAG = "IPosPrinter";
        private static IPosPrinterService mIPosPrinterService;
        private static IPosPrinterCallback callback = null;

        private static final ServiceConnection connectService = new ServiceConnection() {
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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Intent intent = new Intent(this, IPosPrinterService.class);
            startService(intent);
            bindService(intent, connectService, Context.BIND_AUTO_CREATE);
            Log.i(TAG, "Service binding initiated");

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

        @Override
        public void onDestroy() {
            super.onDestroy();
            unbindService(connectService);
            Log.i(TAG, "Service unbinding initiated");
        }

        private static void bindService() {
            try {
                if (context == null) {
                    Log.e(TAG, "Context is null, cannot bind to service");
                    return;
                }

                Intent intent = new Intent();
                String packageName = context.getApplicationContext().getPackageName();
                String cls = "com.conecsa.ipos.printer.service.IPosPrinter";
                String action = "com.conecsa.ipos.printer.IPosPrinter";
                intent.setComponent(new ComponentName(packageName, cls));
                intent.setPackage(cls);
                intent.setAction(action);

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

        public static void printerInit() {
            ThreadPoolManager.getInstance().executeTask(() -> {
                try {
                    mIPosPrinterService.printerInit(callback);
                    int status = mIPosPrinterService.getPrinterStatus();
                    if (status != 0) {
                        Log.e(TAG, "Printer is not ready");
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to initialize printer", e);
                }
            });
        }

        public static boolean printText(String text) {
            ThreadPoolManager.getInstance().executeTask(() -> {
                try {
                    printerInit();
                    mIPosPrinterService.printText(text, callback);
                    mIPosPrinterService.printSpecifiedTypeText(text, "ST", 12, callback);
                    mIPosPrinterService.printerPerformPrint(160, callback);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to print text", e);
                }
            });
            return true;
        }
    }
}
