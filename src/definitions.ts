export interface IPosPrinterPlugin {
  getPrinterStatus(): Promise<{ result: number }>;
  getPrinterStatusMessage(options: {value: number}): Promise<{ result: string }>;
  setPrinterPrintDepth(options: {depth: number}): Promise<{ result: string }>;
  setPrinterPrintFontType(options: {typeface: string}): Promise<{ result: string }>;
  setPrinterPrintFontSize(options: {fontSize: number}): Promise<{ result: string }>;
  setPrinterPrintAlignment(options: {alignment: number}): Promise<{ result: string }>;
  printerFeedLines(options: {lines: number}): Promise<{ result: string }>;
  printBlankLines(options: {lines: number}): Promise<{ result: string }>;
  printText(options: {text: string}): Promise<{ result: string }>;
  printSpecifiedTypeText(options: {text: string, typeface: string, fontSize: number}): Promise<{ result: string }>;
  PrintSpecFormatText(options: {text: string, typeface: string, fontSize: number, alignment: number}): Promise<{ result: string }>;
  printColumnsText(options: {colsTextArr: string[], colsWidthArr: number[], colsAlign: number[]}): Promise<{ result: string }>;
  printBitmap(options: {alignment: number, bitmapSize: number, base64: string}): Promise<{ result: string }>;
  printBarCode(options: {data: string, symbology: number, height: number, width: number, textPosition: number}): Promise<{ result: string }>;
  printQRCode(options: {data: string, moduleSize: number, errorLevel: number}): Promise<{ result: string }>;
  printRawData(options: {data: string}): Promise<{ result: string }>;
  printRowBlock(): Promise<{ result: string }>;
}
