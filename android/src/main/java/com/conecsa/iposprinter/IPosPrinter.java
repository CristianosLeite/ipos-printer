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

import com.conecsa.iposprinter.Utils.BytesUtil;

import java.util.Objects;

public class IPosPrinter extends Service implements IPosPrinterService {
  private final String TAG = "IPosPrinter";
  private IPosPrinterService mIPosPrinterService;
  private IPosPrinterCallback callback;

  // Printer status values
  public final int PRINTER_NORMAL = 0;
  public final int PRINTER_PAPERLESS = 1;
  public final int PRINTER_THP_HIGH_TEMPERATURE = 2;
  public final int PRINTER_MOTOR_HIGH_TEMPERATURE = 3;
  public final int PRINTER_IS_BUSY = 4;
  public final int PRINTER_ERROR_UNKNOWN = 5;

  @Override
  public void onCreate() {
    super.onCreate();
    Log.i(TAG, "Service created");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (mIPosPrinterService != null) {
      mIPosPrinterService = null;
    }
    Log.i(TAG, "Service unbinding");
  }

  @Override
  public IBinder onBind(Intent intent) {
    return mIPosPrinterService.asBinder();
  }

  @Override
  public IBinder asBinder() {
    return mIPosPrinterService.asBinder();
  }

  /**
   * Set the callback to return the result of the operations.
   * @param callback Callback to return the result of the operation
   */
  public void setCallback(IPosPrinterCallback callback) {
    this.callback = callback;
  }

  /**
   * Connect to the service.
   */
  private final ServiceConnection connectService = new ServiceConnection() {

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      // Assigning the service to the interface
      mIPosPrinterService = Stub.asInterface(service);
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

  /**
   * Create a bound service.
   * @param context Application context
   */
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
   * @param operation Function to execute
   */
  private void runAsyncPrinterOperation(AsyncPrinterOperation operation) {
    if (!isServiceConnected()) { return; }
    ThreadPoolManager.getInstance().executeTask(() -> {
      try {
        operation.execute();
      } catch (RemoteException e) {
        Log.e(TAG, "Failed to execute printer operation", e);
      }
    });
  }

  /**
   * Check if proxy service is connected.
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

  /**
   * Get the printer status.
   * <p>Overloads the getPrinterStatus method
   * @param status Printer status
   * @return The printer status message
   */
  String getPrinterStatus(int status) throws RemoteException {
      String printerStatusMessage = switch (status) {
      case PRINTER_NORMAL -> "Printer normal";
      case PRINTER_PAPERLESS -> "Printer paperless";
      case PRINTER_THP_HIGH_TEMPERATURE -> "Printer THP high temperature";
      case PRINTER_MOTOR_HIGH_TEMPERATURE -> "Printer motor high temperature";
      case PRINTER_IS_BUSY -> "Printer is busy";
      case PRINTER_ERROR_UNKNOWN -> "Printer error unknown";
      default -> "Printer status unknown";
    };
      callback.onReturnString(printerStatusMessage);
      return printerStatusMessage;
  }

  /**
   * Get the printer status.
   * @return The current state of the printer
   * <ul>
   * <li>0:PRINTER_NORMAL You can start a new print at this point
   * <li>1:PRINTER_PAPERLESS  At this time, the printing will be stopped. <p>If the current printing is not completed, you need to reprint after adding paper
   * <li>2:PRINTER_THP_HIGH_TEMPERATURE At this time, the printing is paused. <p>If the current printing is not completed, the printing will continue after cooling, and there is no need to reprint
   * <li>3:PRINTER_MOTOR_HIGH_TEMPERATURE In this case, printing is not executed. <p>After cooling, you need to initialize the printer and re-initiate the printing job
   * <li>4:PRINTER_IS_BUSY    The printer is printing at this point
   * <li>5:PRINT_ERROR_UNKNOWN  The printer is abnormal
   * @throws RemoteException Throws a RemoteException if cannot get the printer status.
   */
  @Override
  public int getPrinterStatus() throws RemoteException {
    if (! isServiceConnected()) { return 6; } // Return a status different from the printer default values
    int printerStatus = mIPosPrinterService.getPrinterStatus();
    callback.onReturnString(String.valueOf(printerStatus));
    return printerStatus;
  }

  /**
   * Printer initialization.
   * <p>The printer is powered on and the default settings are initialized.
   * Please check the printer status when using it, and wait when you PRINTER_IS_BUSY.
   * @param callback Callback to return the result of the operation
   * @throws RemoteException Throws a RemoteException if the printer cannot be initialized.
   */
  @Override
  public void printerInit(IPosPrinterCallback callback) throws RemoteException {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printerInit(callback);
      Log.i(TAG, "Printer initialized");
    });
  }

  /**
   * Sets the print font type, which has an effect on subsequent printing, unless initialized.
   * @param depth Concentration level, range 1-10, out of range This function does not perform default level 6
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void setPrinterPrintDepth(int depth, IPosPrinterCallback callback) {
    if (!isServiceConnected()) {
      return;
    }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintDepth(depth, callback);
      callback.onReturnString("Printer depth set to: " + depth);
    });
  }

  /**
   * Sets the print font type, which has an effect on subsequent printing, unless initialized.
   * (Currently, only one font ST is supported, and more fonts will be supported in the future).
   * @param typeface Font Name ST (currently only one type is supported)
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void setPrinterPrintFontType(String typeface, IPosPrinterCallback callback) throws RemoteException {
    if (! isServiceConnected()) { return; }
    if (!Objects.equals(typeface, "ST")) {
      callback.onReturnString("Typeface is not supported");
      return;
    }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintFontType(typeface, callback);
      callback.onReturnString("Typeface set to: " + typeface);
    });
  }

  /**
   * Sets the font size, which has an effect on subsequent printing, unless initialized.
   * <p>Note: The font size is a way to print outside of the standard international directives,
   * Adjusting the font size affects the character width and the number of characters per line will also change.
   * Therefore, the layout formed by monospaced fonts may be out of order and you need to adjust it yourself.
   * @param fontSize Currently, the font size is 16, 24, 32, and 48, and the default value of 24 is executed if the invalid size is entered
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void setPrinterPrintFontSize(int fontSize, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintFontSize(fontSize, callback);
      callback.onReturnString("Font size set to: " + fontSize);
    });
  }

  /**
   * Sets the alignment, which has an effect on subsequent printing, unless initialized.
   * @param alignment Alignment 0--Left, 1--Center, 2--Right, default centering
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void setPrinterPrintAlignment(int alignment, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.setPrinterPrintAlignment(alignment, callback);
      callback.onReturnString("Alignment set to: " + (alignment == 0 ? "Left" : (alignment == 2 ? "Right" : "Center")));
    });
  }

  /**
   * Printer paper transfer (forced line break, paper lines are taken after the previous print content is finished, and the motor idles the paper to move at this time, and no data is transmitted to the printer).
   * @param lines Number of printer lines (each line is a pixel)
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printerFeedLines(int lines, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printerFeedLines(lines, callback);
      Log.i(TAG, "Printer feed lines: " + lines);
    });
  }

  /**
   * Printing blank lines (forced line wrapping, printing blank lines after the previous print, and all the data transferred to the printer is 0x00).
   * @param lines The number of blank lines printed is limited to a maximum of 100 lines
   * @param height The height of a blank line (in pixels)
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printBlankLines(int lines, int height, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printBlankLines(lines, height, callback);
      Log.i(TAG, "Printed blank lines: " + lines);
    });
  }

  /**
   * Print a text.
   * @param text Text wraps at the full width of one line
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printText(String text, IPosPrinterCallback callback) {
      runAsyncPrinterOperation(() -> {
          int maxAttempts = 5;
          int attempts = 0;
          while (attempts < maxAttempts) {
              int printerStatus = getPrinterStatus();
              if (printerStatus == PRINTER_NORMAL) {
                  mIPosPrinterService.printText(text, callback);
                  mIPosPrinterService.printerPerformPrint(160, callback);
                  callback.onRunResult(true);
                  break;
              } else {
                  attempts++;
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      Log.e(TAG, "Interrupted while waiting for printer status", e);
                      callback.onRunResult(false);
                      break;
                  }
              }
          }
      });
  }

  /**
   * Print the text with the specified font type and size. <p>The font settings are only valid for this time.
   * Text wraps at the full width of one line.
   * @param text The text string to print
   * @param typeface Font Name ST (currently only one type is supported)
   * @param fontSize Currently, the font size is 16, 24, 32, and 48, and the default value of 24 is executed if the invalid size is entered
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printSpecifiedTypeText(String text, String typeface, int fontSize, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printSpecifiedTypeText(text, typeface, fontSize, callback);
      Log.i(TAG, "Sent specified type text to printer");
      Log.i(TAG, "Text: " + text);
    });
  }

  /**
   * Print the text with the specified font type and size. <p>The font settings are only valid for this time.
   * Text wraps at the full width of one line.
   * @param text The text string to print
   * @param typeface Font Name ST (currently only one type is supported)
   * @param fontSize Currently, the font size is 16, 24, 32, and 48, and the default value of 24 is executed if the invalid size is entered
   * @param alignment Alignment (0 left, 1 center, 2 right)
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void PrintSpecFormatText(String text, String typeface, int fontSize, int alignment, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.PrintSpecFormatText(text, typeface, fontSize, alignment, callback);
      Log.i(TAG, "Sent specified format text to printer");
      Log.i(TAG, "Text: " + text);
    });
  }

  /**
   * Print a row of the table, you can specify the column width and alignment.
   * @param colsTextArr An array of text strings for each column
   * @param colsWidthArr The total width of each column width array cannot be greater than ((384 / font size) << 1)-(number of columns +1)
   * (calculated as English characters, each Chinese character accounts for two English characters, each width is greater than 0),
   * @param colsAlign Column alignment (0 to the left, 1 to the center, 2 to the right)
   * @param isContinuousPrint Whether to continue printing Form 1: Continue printing 0: Do not continue printing
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printColumnsText(String[] colsTextArr, int[] colsWidthArr, int[] colsAlign, int isContinuousPrint, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printColumnsText(colsTextArr, colsWidthArr, colsAlign, isContinuousPrint, callback);
      Log.i(TAG, "Sent columns text to printer");
    });
  }

  /**
   * Print a picture.
   * @param alignment Alignment 0--Left, 1--Center, 2--Right, Default Centering
   * @param bitmapSize Bitmap size, the input size range is 1~16, and the default selection is 10 units: 24bit
   * @param mBitmap Image bitmap object (maximum width 384 pixels, beyond which it cannot be printed and callback exception function)
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printBitmap(int alignment, int bitmapSize, Bitmap mBitmap, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printBitmap(alignment, bitmapSize, mBitmap, callback);
      Log.i(TAG, "Sent bitmap to printer");
    });
  }

  /**
   * Print a barcode.
   * @param data Barcode data
   * @param symbology Barcode type
   * <ul>
   * <li>0: UPC-A <li>1: UPC-E <li>2: JAN13(EAN13) <li>3: JAN8(EAN8) <li>4: CODE39 <li>5: ITF <li>6: CODABAR <li>7: CODE93 <li>8: CODE128
   * @param height The height of the barcode can be from 1 to 16, and the default value is 6 when the range is exceeded, and each unit represents the height of 24 pixels
   * @param width The width of the barcode, the value is 1 to 16, and the default value is 12 when the range is exceeded, and each unit represents the length of 24 pixels
   * @param textPosition Text position 0--do not print text, 1--text above the barcode, 2--text below the barcode, 3--print on the top and bottom of the barcode
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printBarCode(String data, int symbology, int height, int width, int textPosition, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printBarCode(data, symbology, height, width, textPosition, callback);
      Log.i(TAG, "Sent barcode to printer");
    });
  }

  /**
   * Print a QrCode.
   * @param data QR code data
   * @param moduleSize QR code block size (unit: points, value from 1 to 16), exceeding the set range defaults to 10.
   * @param mErrorCorrectionLevel 2D error correction level (0:L 1:M 2:Q 3:H)
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printQRCode(String data, int moduleSize, int mErrorCorrectionLevel, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printQRCode(data, moduleSize, mErrorCorrectionLevel, callback);
      Log.i(TAG, "Sent QR code to printer");
    });
  }

  /**
   * Print the raw byte data.
   * @param rawPrintData Byte data block
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printRawData(byte[] rawPrintData, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printRawData(rawPrintData, callback);
      Log.i(TAG, "Sent raw data to printer");
    });
  }

  /**
   * Print using ESC/POS commands.
   * @param data ESC/POS commands
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void sendUserCMDData(byte[] data, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.sendUserCMDData(data, callback);
      Log.i(TAG, "Sent user command data to printer");
    });
  }

  /**
   * <h5>Performs a print operation.
   * <p>After each print function method is executed, this method is required for the printer to print.
   * @param feedLines Number of lines to feed after the print operation
   * @param callback Callback to return the result of the operation
   */
  @Override
  public void printerPerformPrint(int feedLines, IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printerPerformPrint(feedLines, callback);
      Log.i(TAG, "Performed print");
    });
  }

  /**
   * Prints a row of blocks
   * @param callback Callback to return the result of the operation
   */
  public void printRowBlock(IPosPrinterCallback callback) {
    if (! isServiceConnected()) { return; }
    runAsyncPrinterOperation(() -> {
      mIPosPrinterService.printRawData(BytesUtil.initLine1(384, 1), callback);
      Log.i(TAG, "Sent block line to printer");
    });
  }
}