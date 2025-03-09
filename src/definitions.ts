export interface IPosPrinterPlugin {
  /**
   * Get the printer status.
   * @returns The current state of the printer
   * @description 0:PRINTER_NORMAL You can start a new print at this point
   * @description 1:PRINTER_PAPERLESS  At this time, the printing will be stopped. <p>If the current printing is not completed, you need to reprint after adding paper
   * @description 2:PRINTER_THP_HIGH_TEMPERATURE At this time, the printing is paused. <p>If the current printing is not completed, the printing will continue after cooling, and there is no need to reprint
   * @description 3:PRINTER_MOTOR_HIGH_TEMPERATURE In this case, printing is not executed. <p>After cooling, you need to initialize the printer and re-initiate the printing job
   * @description 4:PRINTER_IS_BUSY    The printer is printing at this point
   * @description 5:PRINT_ERROR_UNKNOWN  The printer is abnormal
   * @throws Throws a RemoteException if cannot get the printer status.
   */
  getPrinterStatus(): Promise<{ result: number }>;
  /**
   * Get the printer status message.
   * @param options Object containing status value
   * @example {status: 0} // Printer normal
   * @returns The message of the printer status
   */
  getPrinterStatusMessage(options: {status: number}): Promise<{ result: string }>;
    /**
   * Sets the print font type, which has an effect on subsequent printing, unless initialized.
   * @param options Object containing depth value
   * @example {depth: 1} // Set the print depth to 1
   * @description depth: concentration level, range 1-10, out of range This function does not perform default level 6
   * @returns The result of the operation
   */
  setPrinterPrintDepth(options: {depth: number}): Promise<{ result: string }>;
    /**
   * Sets the print font type, which has an effect on subsequent printing, unless initialized.
   * (Currently, only one font ST is supported, and more fonts will be supported in the future).
   * @param options Object containing typeface value
   * @example {typeface: 'ST'} // Set the print font type to ST
   */
  setPrinterPrintFontType(options: {typeface: string}): Promise<{ result: string }>;
    /**
   * Sets the print font size, which has an effect on subsequent printing, unless initialized.
   * Currently, the font size is 16, 24, 32, and 48, and the default value of 24 is executed if the invalid size is entered
   * @param options Object containing fontSize value
   * @example {fontSize: 32} // Set the print font size to 32
   */
  setPrinterPrintFontSize(options: {fontSize: number}): Promise<{ result: string }>;
    /**
   * Sets the print alignment, which has an effect on subsequent printing, unless initialized.
   * @param options Object containing alignment value
   * @property alignment: 0: left alignment, 1: center alignment, 2: right alignment
   * @example {alignment: 1} // Set the print alignment to center
   */
  setPrinterPrintAlignment(options: {alignment: number}): Promise<{ result: string }>;
    /**
   * Printing blank lines (forced line wrapping, printing blank lines after the previous print, and all the data transferred to the printer is 0x00).
   * @param options Object containing lines quantity and height
   * @example {lines: 3, height: 24} // Print 3 blank lines with a height of 24 pixels
   * @returns The result of the operation
   */
  printBlankLines(options: {lines: number, height: number}): Promise<{ result: string }>;
    /**
   * Print a text.
   * @param options Object containing text value
   * @example {text: 'Hello, World!'} // Print the text 'Hello, World!' 
   * @returns The result of the operation
   */
  printText(options: {text: string}): Promise<{ result: string }>;
    /**
   * Print a text with specified type.
   * @param options Object containing text, typeface and fontSize values
   * @example {text: 'Hello, World!', typeface: 'ST', fontSize: 24} // Print the text 'Hello, World!' with typeface ST and font size 24
   * @returns The result of the operation
   */
  printSpecifiedTypeText(options: {text: string, typeface: string, fontSize: number}): Promise<{ result: string }>;
    /**
   * Print a text with specified format.
   * @param options Object containing text, typeface, fontSize and alignment values
   * @example {text: 'Hello, World!', typeface: 'ST', fontSize: 24, alignment: 1} // Print the text 'Hello, World!' with typeface ST, font size 24 and center alignment
   * @returns The result of the operation
   */
  PrintSpecFormatText(options: {text: string, typeface: string, fontSize: number, alignment: number}): Promise<{ result: string }>;
    /**
   * Print a text with specified columns.
   * @param options Object containing colsTextArr, colsWidthArr, colsAlignArr and isContinuousPrint values
   * @example {colsTextArr: ['Hello', 'World'], colsWidthArr: [24, 24], colsAlignArr: [0, 1], isContinuousPrint: 1} // Print the text 'Hello' and 'World' in two columns with widths 24 and 24, and left and center alignment
   * @description colsTextArr: Text array of each column
   * @description colsWidthArr The total width of each column width array cannot be greater than ((384 / font size) << 1)-(number of columns +1)
   * @description colsAlignArr: Column alignment (0 to the left, 1 to the center, 2 to the right)
   * @description isContinuousPrint: Whether to continue printing Form 1: Continue printing 0: Do not continue printing
   * @returns The result of the operation
   */
  printColumnsText(options: {colsTextArr: string[], colsWidthArr: number[], colsAlignArr: number[], isContinuousPrint: number}): Promise<{ result: string }>;
  /**
   * Print a picture.
   * @param options Object containing alignment, bitmapSize and base64 values
   * @example {alignment: 1, bitmapSize: 20, base64: 'iVBORw0KGgoAAAANSUhEUgAAABgAAAAYCAYAAADgdz34'} // Print the picture with center alignment, size 20 and base64 data
   * @returns The result of the operation
   */
  printBitmap(options: {alignment: number, bitmapSize: number, base64: string}): Promise<{ result: string }>;
      /**
   * Print a barcode.
   * @param options Object containing data, symbology, height, width and textPosition values
   * @example {data: '1234567890', symbology: 1, height: 60, width: 120, textPosition: 0} // Print the barcode '1234567890' with symbology 1, height 60, width 120 and no text
   * @description symbology: Barcode type, 0: UPC-A, 1: UPC-E, 2: JAN13 (EAN13), 3: JAN8 (EAN8), 4: CODE39, 5: ITF, 6: CODABAR, 7: CODE93, 8: CODE128
   * @description height: The height of the barcode can be from 1 to 16, and the default value is 6 when the range is exceeded, and each unit represents the height of 24 pixels
   * @description width: The width of the barcode, the value is 1 to 16, and the default value is 12 when the range is exceeded, and each unit represents the length of 24 pixels
   * @description textPosition: Text position 0--do not print text, 1--text above the barcode, 2--text below the barcode, 3--print on the top and bottom of the barcode
   * @returns The result of the operation
   */
  printBarCode(options: {data: string, symbology: number, height: number, width: number, textPosition: number}): Promise<{ result: string }>;
    /**
   * Print a QR code.
   * @param options Object containing data, moduleSize and errorCorrectionLevel values
   * @example {data: 'Hello, World!', moduleSize: 5, errorCorrectionLevel: 1} // Print the QR code 'Hello, World!' with size 5 and error correction level 1
   * @description moduleSize: QR code block size (unit: points, value from 1 to 16), exceeding the set range defaults to 10.
   * @description errorCorrectionLevel: Error correction level, 0--L, 1--M, 2--Q, 3--H
   * @returns The result of the operation
   */
  printQRCode(options: {data: string, moduleSize: number, errorCorrectionLevel: number}): Promise<{ result: string }>;
    /**
   * Print the raw byte data.
   * @param options Object containing data value
   * @example {data: 'Hello, World!'} // Print 'Hello, World!' in raw data
   * @description data: Byte data block
   * @returns The result of the operation
   */
  printRawData(options: {data: string}): Promise<{ result: string }>;
    /**
   * Print a row block.
   * @returns The result of the operation
   */
  printRowBlock(): Promise<{ result: string }>;
}
