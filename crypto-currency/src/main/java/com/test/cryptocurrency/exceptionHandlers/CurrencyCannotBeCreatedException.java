package com.test.cryptocurrency.exceptionHandlers;


public class CurrencyCannotBeCreatedException extends RuntimeException {
	@java.io.Serial
    static final long serialVersionUID = 1L;

    private String tickerCode;

    public CurrencyCannotBeCreatedException(String ticker) {
        this.tickerCode = ticker;
    }
    
    public String GetCurrencyTickerCode() {
        return this.tickerCode;
    }

}