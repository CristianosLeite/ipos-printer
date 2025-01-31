# ipos-printer

Plugin for Q2i PDA printer

## Install

```bash
npm install ipos-printer
npx cap sync
```

## API

<docgen-index>

* [`getPrinterStatus()`](#getprinterstatus)
* [`getPrinterStatusMessage(...)`](#getprinterstatusmessage)
* [`setPrinterPrintDepth(...)`](#setprinterprintdepth)
* [`setPrinterPrintFontType(...)`](#setprinterprintfonttype)
* [`setPrinterPrintFontSize(...)`](#setprinterprintfontsize)
* [`setPrinterPrintAlignment(...)`](#setprinterprintalignment)
* [`printerFeedLines(...)`](#printerfeedlines)
* [`printBlankLines(...)`](#printblanklines)
* [`printText(...)`](#printtext)
* [`printSpecifiedTypeText(...)`](#printspecifiedtypetext)
* [`PrintSpecFormatText(...)`](#printspecformattext)
* [`printColumnsText(...)`](#printcolumnstext)
* [`printBitmap(...)`](#printbitmap)
* [`printBarCode(...)`](#printbarcode)
* [`printQRCode(...)`](#printqrcode)
* [`printRawData(...)`](#printrawdata)
* [`printRowBlock()`](#printrowblock)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### getPrinterStatus()

```typescript
getPrinterStatus() => Promise<{ result: number; }>
```

Get the printer status.

**Returns:** <code>Promise&lt;{ result: number; }&gt;</code>

--------------------


### getPrinterStatusMessage(...)

```typescript
getPrinterStatusMessage(options: { status: number; }) => Promise<{ result: string; }>
```

Get the printer status message.

| Param         | Type                             | Description                    |
| ------------- | -------------------------------- | ------------------------------ |
| **`options`** | <code>{ status: number; }</code> | Object containing status value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintDepth(...)

```typescript
setPrinterPrintDepth(options: { depth: number; }) => Promise<{ result: string; }>
```

Sets the print font type, which has an effect on subsequent printing, unless initialized.

| Param         | Type                            | Description                   |
| ------------- | ------------------------------- | ----------------------------- |
| **`options`** | <code>{ depth: number; }</code> | Object containing depth value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintFontType(...)

```typescript
setPrinterPrintFontType(options: { typeface: string; }) => Promise<{ result: string; }>
```

Sets the print font type, which has an effect on subsequent printing, unless initialized.
(Currently, only one font ST is supported, and more fonts will be supported in the future).

| Param         | Type                               | Description                      |
| ------------- | ---------------------------------- | -------------------------------- |
| **`options`** | <code>{ typeface: string; }</code> | Object containing typeface value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintFontSize(...)

```typescript
setPrinterPrintFontSize(options: { fontSize: number; }) => Promise<{ result: string; }>
```

Sets the print font size, which has an effect on subsequent printing, unless initialized.
Currently, the font size is 16, 24, 32, and 48, and the default value of 24 is executed if the invalid size is entered

| Param         | Type                               | Description                      |
| ------------- | ---------------------------------- | -------------------------------- |
| **`options`** | <code>{ fontSize: number; }</code> | Object containing fontSize value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintAlignment(...)

```typescript
setPrinterPrintAlignment(options: { alignment: number; }) => Promise<{ result: string; }>
```

Sets the print alignment, which has an effect on subsequent printing, unless initialized.

| Param         | Type                                | Description                       |
| ------------- | ----------------------------------- | --------------------------------- |
| **`options`** | <code>{ alignment: number; }</code> | Object containing alignment value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printerFeedLines(...)

```typescript
printerFeedLines(options: { lines: number; }) => Promise<{ result: string; }>
```

Printer paper transfer (forced line break, paper lines are taken after the previous print content is finished, and the motor idles the paper to move at this time, and no data is transmitted to the printer).

| Param         | Type                            | Description                      |
| ------------- | ------------------------------- | -------------------------------- |
| **`options`** | <code>{ lines: number; }</code> | Object containing lines quantity |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printBlankLines(...)

```typescript
printBlankLines(options: { lines: number; height: number; }) => Promise<{ result: string; }>
```

Printing blank lines (forced line wrapping, printing blank lines after the previous print, and all the data transferred to the printer is 0x00).

| Param         | Type                                            | Description                                 |
| ------------- | ----------------------------------------------- | ------------------------------------------- |
| **`options`** | <code>{ lines: number; height: number; }</code> | Object containing lines quantity and height |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printText(...)

```typescript
printText(options: { text: string; }) => Promise<{ result: string; }>
```

Print a text.

| Param         | Type                           | Description                  |
| ------------- | ------------------------------ | ---------------------------- |
| **`options`** | <code>{ text: string; }</code> | Object containing text value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printSpecifiedTypeText(...)

```typescript
printSpecifiedTypeText(options: { text: string; typeface: string; fontSize: number; }) => Promise<{ result: string; }>
```

Print a text with specified type.

| Param         | Type                                                               | Description                                          |
| ------------- | ------------------------------------------------------------------ | ---------------------------------------------------- |
| **`options`** | <code>{ text: string; typeface: string; fontSize: number; }</code> | Object containing text, typeface and fontSize values |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### PrintSpecFormatText(...)

```typescript
PrintSpecFormatText(options: { text: string; typeface: string; fontSize: number; alignment: number; }) => Promise<{ result: string; }>
```

Print a text with specified format.

| Param         | Type                                                                                  | Description                                                     |
| ------------- | ------------------------------------------------------------------------------------- | --------------------------------------------------------------- |
| **`options`** | <code>{ text: string; typeface: string; fontSize: number; alignment: number; }</code> | Object containing text, typeface, fontSize and alignment values |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printColumnsText(...)

```typescript
printColumnsText(options: { colsTextArr: string[]; colsWidthArr: number[]; colsAlignArr: number[]; isContinuousPrint: number; }) => Promise<{ result: string; }>
```

Print a text with specified columns.

| Param         | Type                                                                                                               | Description                                                                            |
| ------------- | ------------------------------------------------------------------------------------------------------------------ | -------------------------------------------------------------------------------------- |
| **`options`** | <code>{ colsTextArr: string[]; colsWidthArr: number[]; colsAlignArr: number[]; isContinuousPrint: number; }</code> | Object containing colsTextArr, colsWidthArr, colsAlignArr and isContinuousPrint values |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printBitmap(...)

```typescript
printBitmap(options: { alignment: number; bitmapSize: number; base64: string; }) => Promise<{ result: string; }>
```

Print a picture.

| Param         | Type                                                                    | Description                                               |
| ------------- | ----------------------------------------------------------------------- | --------------------------------------------------------- |
| **`options`** | <code>{ alignment: number; bitmapSize: number; base64: string; }</code> | Object containing alignment, bitmapSize and base64 values |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printBarCode(...)

```typescript
printBarCode(options: { data: string; symbology: number; height: number; width: number; textPosition: number; }) => Promise<{ result: string; }>
```

Print a barcode.

| Param         | Type                                                                                                   | Description                                                              |
| ------------- | ------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------ |
| **`options`** | <code>{ data: string; symbology: number; height: number; width: number; textPosition: number; }</code> | Object containing data, symbology, height, width and textPosition values |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printQRCode(...)

```typescript
printQRCode(options: { data: string; moduleSize: number; errorCorrectionLevel: number; }) => Promise<{ result: string; }>
```

Print a QR code.

| Param         | Type                                                                             | Description                                                        |
| ------------- | -------------------------------------------------------------------------------- | ------------------------------------------------------------------ |
| **`options`** | <code>{ data: string; moduleSize: number; errorCorrectionLevel: number; }</code> | Object containing data, moduleSize and errorCorrectionLevel values |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printRawData(...)

```typescript
printRawData(options: { data: string; }) => Promise<{ result: string; }>
```

Print the raw byte data.

| Param         | Type                           | Description                  |
| ------------- | ------------------------------ | ---------------------------- |
| **`options`** | <code>{ data: string; }</code> | Object containing data value |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printRowBlock()

```typescript
printRowBlock() => Promise<{ result: string; }>
```

Print a row block.

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------

</docgen-api>
