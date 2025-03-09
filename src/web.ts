import { WebPlugin } from '@capacitor/core';

import type { IPosPrinterPlugin } from './definitions';

export class IPosPrinterWeb extends WebPlugin implements IPosPrinterPlugin {
  getPrinterStatus(): Promise<{ result: number; }> {
    console.log('getPrinterStatus');
    return Promise.resolve({ result: 0 });
  }
  getPrinterStatusMessage(options: { status: number; }): Promise<{ result: string; }> {
    console.log('getPrinterStatusMessage', options);
    return Promise.resolve({ result: 'Success' });
  }
  setPrinterPrintDepth(options: { depth: number; }): Promise<{ result: string; }> {
    console.log('setPrinterPrintDepth', options);
    return Promise.resolve({ result: 'Success' });
  }
  setPrinterPrintFontType(options: { typeface: string; }): Promise<{ result: string; }> {
    console.log('setPrinterPrintFontType', options);
    return Promise.resolve({ result: 'Success' });
  }
  setPrinterPrintFontSize(options: { fontSize: number; }): Promise<{ result: string; }> {
    console.log('setPrinterPrintFontSize', options);
    return Promise.resolve({ result: 'Success' });
  }
  setPrinterPrintAlignment(options: { alignment: number; }): Promise<{ result: string; }> {
    console.log('setPrinterPrintAlignment', options);
    return Promise.resolve({ result: 'Success' });
  }
  printBlankLines(options: { lines: number; height: number; }): Promise<{ result: string; }> {
    console.log('printBlankLines', options);
    return Promise.resolve({ result: 'Success' });
  }
  printText(options: { text: string; }): Promise<{ result: string; }> {
    console.log('printText', options);
    return Promise.resolve({ result: 'Success' });
  }
  printSpecifiedTypeText(options: { text: string; typeface: string; fontSize: number; }): Promise<{ result: string; }> {
    console.log('printSpecifiedTypeText', options);
    return Promise.resolve({ result: 'Success' });
  }
  PrintSpecFormatText(options: { text: string; typeface: string; fontSize: number; alignment: number; }): Promise<{ result: string; }> {
    console.log('PrintSpecFormatText', options);
    return Promise.resolve({ result: 'Success' });
  }
  printColumnsText(options: { colsTextArr: string[]; colsWidthArr: number[]; colsAlignArr: number[]; isContinuousPrint: number; }): Promise<{ result: string; }> {
    console.log('printColumnsText', options);
    return Promise.resolve({ result: 'Success' });
  }
  printBitmap(options: { alignment: number; bitmapSize: number; base64: string; }): Promise<{ result: string; }> {
    console.log('printBitmap', options);
    return Promise.resolve({ result: 'Success' });
  }
  printBarCode(options: { data: string; symbology: number; height: number; width: number; textPosition: number; }): Promise<{ result: string; }> {
    console.log('printBarCode', options);
    return Promise.resolve({ result: 'Success' });
  }
  printQRCode(options: { data: string; moduleSize: number; errorCorrectionLevel: number; }): Promise<{ result: string; }> {
    console.log('printQRCode', options);
    return Promise.resolve({ result: 'Success' });
  }
  printRawData(options: { data: string; }): Promise<{ result: string; }> {
    console.log('printRawData', options);
    return Promise.resolve({ result: 'Success' });
  }
  printRowBlock(): Promise<{ result: string; }> {
    console.log('printRowBlock');
    return Promise.resolve({ result: 'Success' });
  }
}
