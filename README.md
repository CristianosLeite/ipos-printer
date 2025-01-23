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

**Returns:** <code>Promise&lt;{ result: number; }&gt;</code>

--------------------


### getPrinterStatusMessage(...)

```typescript
getPrinterStatusMessage(options: { value: number; }) => Promise<{ result: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ value: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintDepth(...)

```typescript
setPrinterPrintDepth(options: { depth: number; }) => Promise<{ result: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ depth: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintFontType(...)

```typescript
setPrinterPrintFontType(options: { typeface: string; }) => Promise<{ result: string; }>
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ typeface: string; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintFontSize(...)

```typescript
setPrinterPrintFontSize(options: { fontSize: number; }) => Promise<{ result: string; }>
```

| Param         | Type                               |
| ------------- | ---------------------------------- |
| **`options`** | <code>{ fontSize: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### setPrinterPrintAlignment(...)

```typescript
setPrinterPrintAlignment(options: { alignment: number; }) => Promise<{ result: string; }>
```

| Param         | Type                                |
| ------------- | ----------------------------------- |
| **`options`** | <code>{ alignment: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printerFeedLines(...)

```typescript
printerFeedLines(options: { lines: number; }) => Promise<{ result: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ lines: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printBlankLines(...)

```typescript
printBlankLines(options: { lines: number; }) => Promise<{ result: string; }>
```

| Param         | Type                            |
| ------------- | ------------------------------- |
| **`options`** | <code>{ lines: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printText(...)

```typescript
printText(options: { text: string; }) => Promise<{ result: string; }>
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ text: string; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printSpecifiedTypeText(...)

```typescript
printSpecifiedTypeText(options: { text: string; typeface: string; fontSize: number; }) => Promise<{ result: string; }>
```

| Param         | Type                                                               |
| ------------- | ------------------------------------------------------------------ |
| **`options`** | <code>{ text: string; typeface: string; fontSize: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### PrintSpecFormatText(...)

```typescript
PrintSpecFormatText(options: { text: string; typeface: string; fontSize: number; alignment: number; }) => Promise<{ result: string; }>
```

| Param         | Type                                                                                  |
| ------------- | ------------------------------------------------------------------------------------- |
| **`options`** | <code>{ text: string; typeface: string; fontSize: number; alignment: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printColumnsText(...)

```typescript
printColumnsText(options: { colsTextArr: string[]; colsWidthArr: number[]; colsAlign: number[]; }) => Promise<{ result: string; }>
```

| Param         | Type                                                                                 |
| ------------- | ------------------------------------------------------------------------------------ |
| **`options`** | <code>{ colsTextArr: string[]; colsWidthArr: number[]; colsAlign: number[]; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printBitmap(...)

```typescript
printBitmap(options: { alignment: number; bitmapSize: number; base64: string; }) => Promise<{ result: string; }>
```

| Param         | Type                                                                    |
| ------------- | ----------------------------------------------------------------------- |
| **`options`** | <code>{ alignment: number; bitmapSize: number; base64: string; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printBarCode(...)

```typescript
printBarCode(options: { data: string; symbology: number; height: number; width: number; textPosition: number; }) => Promise<{ result: string; }>
```

| Param         | Type                                                                                                   |
| ------------- | ------------------------------------------------------------------------------------------------------ |
| **`options`** | <code>{ data: string; symbology: number; height: number; width: number; textPosition: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printQRCode(...)

```typescript
printQRCode(options: { data: string; moduleSize: number; errorLevel: number; }) => Promise<{ result: string; }>
```

| Param         | Type                                                                   |
| ------------- | ---------------------------------------------------------------------- |
| **`options`** | <code>{ data: string; moduleSize: number; errorLevel: number; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printRawData(...)

```typescript
printRawData(options: { data: string; }) => Promise<{ result: string; }>
```

| Param         | Type                           |
| ------------- | ------------------------------ |
| **`options`** | <code>{ data: string; }</code> |

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------


### printRowBlock()

```typescript
printRowBlock() => Promise<{ result: string; }>
```

**Returns:** <code>Promise&lt;{ result: string; }&gt;</code>

--------------------

</docgen-api>
