import { WebPlugin } from '@capacitor/core';

import type { IposPrinterPlugin } from './definitions';

export class IposPrinterWeb extends WebPlugin implements IposPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
