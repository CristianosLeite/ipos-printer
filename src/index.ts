import { registerPlugin } from '@capacitor/core';

import type { IposPrinterPlugin } from './definitions';

const IposPrinter = registerPlugin<IposPrinterPlugin>('IposPrinter', {
  web: () => import('./web').then((m) => new m.IposPrinterWeb()),
});

export * from './definitions';
export { IposPrinter };
