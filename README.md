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
getPrinterStatus() => Promise<{ status: number; }>
```

**Returns:** <code>Promise&lt;{ status: number; }&gt;</code>

--------------------


### getPrinterStatusMessage(...)

```typescript
getPrinterStatusMessage(options: { value: number; }) => Promise<{ value: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: number; }</code> |

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### setPrinterPrintDepth(...)

```typescript
setPrinterPrintDepth(options: { depth: number; }) => Promise<void>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ depth: number; }</code> |

--------------------


### setPrinterPrintFontType(...)

```typescript
setPrinterPrintFontType(options: { typeface: string; }) => Promise<void>
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ typeface: string; }</code> |

--------------------


### setPrinterPrintFontSize(...)

```typescript
setPrinterPrintFontSize(options: { fontSize: number; }) => Promise<void>
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ fontSize: number; }</code> |

--------------------


### setPrinterPrintAlignment(...)

```typescript
setPrinterPrintAlignment(options: { alignment: number; }) => Promise<void>
```

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ alignment: number; }</code> |

--------------------


### printerFeedLines(...)

```typescript
printerFeedLines(options: { lines: number; }) => Promise<void>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ lines: number; }</code> |

--------------------


### printBlankLines(...)

```typescript
printBlankLines(options: { lines: number; }) => Promise<void>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ lines: number; }</code> |

--------------------


### printText(...)

```typescript
printText(options: { text: string; }) => Promise<void>
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ text: string; }</code> |

--------------------


### printSpecifiedTypeText(...)

```typescript
printSpecifiedTypeText(options: { text: string; typeface: string; fontSize: number; }) => Promise<void>
```

| Param         | Type                                                               |
| ------------- | ------------------------------------------------------------------ |
| **`options`** | <code>{ text: string; typeface: string; fontSize: number; }</code> |

--------------------


### PrintSpecFormatText(...)

```typescript
PrintSpecFormatText(options: { text: string; typeface: string; fontSize: number; alignment: number; }) => Promise<void>
```

| Param         | Type                                                                                  |
| ------------- | ------------------------------------------------------------------------------------- |
| **`options`** | <code>{ text: string; typeface: string; fontSize: number; alignment: number; }</code> |

--------------------


### printColumnsText(...)

```typescript
printColumnsText(options: { colsTextArr: string[]; colsWidthArr: number[]; colsAlign: number[]; }) => Promise<void>
```

| Param         | Type                                                                                 |
| ------------- | ------------------------------------------------------------------------------------ |
| **`options`** | <code>{ colsTextArr: string[]; colsWidthArr: number[]; colsAlign: number[]; }</code> |

--------------------


### printBitmap(...)

```typescript
printBitmap(options: { alignment: number; bitmapSize: number; base64: string; }) => Promise<void>
```

| Param         | Type                                                                    |
| ------------- | ----------------------------------------------------------------------- |
| **`options`** | <code>{ alignment: number; bitmapSize: number; base64: string; }</code> |

--------------------


### printBarCode(...)

```typescript
printBarCode(options: { data: string; symbology: number; height: number; width: number; textPosition: number; }) => Promise<void>
```

| Param         | Type                                                                                                   |
| ------------- | ------------------------------------------------------------------------------------------------------ |
| **`options`** | <code>{ data: string; symbology: number; height: number; width: number; textPosition: number; }</code> |

--------------------


### printQRCode(...)

```typescript
printQRCode(options: { data: string; moduleSize: number; errorLevel: number; }) => Promise<void>
```

| Param         | Type                                                                   |
| ------------- | ---------------------------------------------------------------------- |
| **`options`** | <code>{ data: string; moduleSize: number; errorLevel: number; }</code> |

--------------------


### printRawData(...)

```typescript
printRawData(options: { data: string; }) => Promise<void>
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ data: string; }</code> |

--------------------


### printRowBlock()

```typescript
printRowBlock() => Promise<void>
```

--------------------

</docgen-api>
