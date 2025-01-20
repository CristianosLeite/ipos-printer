import { registerPlugin } from '@capacitor/core';

import type { IPosPrinterPlugin } from './definitions';

const IPosPrinter = registerPlugin<IPosPrinterPlugin>('IPosPrinter', {
  web: () => import('./web').then((m) => new m.IPosPrinterWeb()),
});

export * from './definitions';
export { IPosPrinter };
