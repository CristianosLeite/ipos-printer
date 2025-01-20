import { WebPlugin } from '@capacitor/core';

import type { IPosPrinterPlugin } from './definitions';

export class IPosPrinterWeb extends WebPlugin implements IPosPrinterPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }

  async print(options: { value: string }): Promise<{ value: string }> {
    console.log('PRINT', options);
    return { value: 'Label printed' };
  }
}
