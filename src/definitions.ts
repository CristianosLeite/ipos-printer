export interface IPosPrinterPlugin {
  getPrinterStatus(): Promise<{ status: number }>;
  getPrinterStatusMessage(options: {value: number}): Promise<{ value: string }>;
  setPrinterPrintDepth(options: {depth: number}): Promise<void>;
  setPrinterPrintFontType(options: {typeface: string}): Promise<void>;
  setPrinterPrintFontSize(options: {fontSize: number}): Promise<void>;
  setPrinterPrintAlignment(options: {alignment: number}): Promise<void>;
  printerFeedLines(options: {lines: number}): Promise<void>;
  printBlankLines(options: {lines: number}): Promise<void>;
  printText(options: {text: string}): Promise<void>;
  printSpecifiedTypeText(options: {text: string, typeface: string, fontSize: number}): Promise<void>;
  PrintSpecFormatText(options: {text: string, typeface: string, fontSize: number, alignment: number}): Promise<void>;
  printColumnsText(options: {colsTextArr: string[], colsWidthArr: number[], colsAlign: number[]}): Promise<void>;
  printBitmap(options: {alignment: number, bitmapSize: number, base64: string}): Promise<void>;
  printBarCode(options: {data: string, symbology: number, height: number, width: number, textPosition: number}): Promise<void>;
  printQRCode(options: {data: string, moduleSize: number, errorLevel: number}): Promise<void>;
  printRawData(options: {data: string}): Promise<void>;
  printRowBlock(): Promise<void>;
}
