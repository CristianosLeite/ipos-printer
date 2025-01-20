package com.conecsa.ipos.printer.service;

interface IPosPrinterCallback {
	oneway void onRunResult(boolean isSuccess);
	oneway void onReturnString(String result);
}
