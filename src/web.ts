import { WebPlugin } from '@capacitor/core';

import type { IPosPrinterPlugin } from './definitions';

export class IPosPrinterWeb extends WebPlugin implements IPosPrinterPlugin {
  getPrinterStatus(): Promise<{ status: number; }> {
    console.log('getPrinterStatus');
    return Promise.resolve({ status: 0 });
  }
  getPrinterStatusMessage(options: { value: number; }): Promise<{ value: string; }> {
    console.log('getPrinterStatusMessage', options);
    return Promise.resolve({ value: 'Success' });
  }
  setPrinterPrintDepth(options: { depth: number; }): Promise<void> {
    console.log('setPrinterPrintDepth', options);
    return Promise.resolve();
  }
  setPrinterPrintFontType(options: { typeface: string; }): Promise<void> {
    console.log('setPrinterPrintFontType', options);
    return Promise.resolve();
  }
  setPrinterPrintFontSize(options: { fontSize: number; }): Promise<void> {
    console.log('setPrinterPrintFontSize', options);
    return Promise.resolve();
  }
  setPrinterPrintAlignment(options: { alignment: number; }): Promise<void> {
    console.log('setPrinterPrintAlignment', options);
    return Promise.resolve();
  }
  printerFeedLines(options: { lines: number; }): Promise<void> {
    console.log('printerFeedLines', options);
    return Promise.resolve();
  }
  printBlankLines(options: { lines: number; }): Promise<void> {
    console.log('printBlankLines', options);
    return Promise.resolve();
  }
  printText(options: { text: string; }): Promise<void> {
    console.log('printText', options);
    return Promise.resolve();
  }
  printSpecifiedTypeText(options: { text: string; typeface: string; fontSize: number; }): Promise<void> {
    console.log('printSpecifiedTypeText', options);
    return Promise.resolve();
  }
  PrintSpecFormatText(options: { text: string; typeface: string; fontSize: number; alignment: number; }): Promise<void> {
    console.log('PrintSpecFormatText', options);
    return Promise.resolve();
  }
  printColumnsText(options: { colsTextArr: string[]; colsWidthArr: number[]; colsAlign: number[]; }): Promise<void> {
    console.log('printColumnsText', options);
    return Promise.resolve();
  }
  printBitmap(options: { alignment: number; bitmapSize: number; base64: string; }): Promise<void> {
    console.log('printBitmap', options);
    return Promise.resolve();
  }
  printBarCode(options: { data: string; symbology: number; height: number; width: number; textPosition: number; }): Promise<void> {
    console.log('printBarCode', options);
    return Promise.resolve();
  }
  printQRCode(options: { data: string; moduleSize: number; errorLevel: number; }): Promise<void> {
    console.log('printQRCode', options);
    return Promise.resolve();
  }
  printRawData(options: { data: string; }): Promise<void> {
    console.log('printRawData', options);
    return Promise.resolve();
  }
  printRowBlock(): Promise<void> {
    console.log('printRowBlock');
    return Promise.resolve();
  }
}
