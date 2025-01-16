export interface IposPrinterPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
