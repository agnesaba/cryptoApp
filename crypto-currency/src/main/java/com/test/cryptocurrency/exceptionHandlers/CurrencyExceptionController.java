package com.test.cryptocurrency.exceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CurrencyExceptionController {
    @ExceptionHandler(value = CurrencyCannotBeCreatedException.class)
    public ResponseEntity<Object> currencyNotCreatedException(CurrencyCannotBeCreatedException exception) {
       return new ResponseEntity<>("Currency: " + exception.GetCurrencyTickerCode() + " already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = CurrencyNotFoundException.class)
    public ResponseEntity<Object> currencyNotFoundException(CurrencyNotFoundException exception) {
       return new ResponseEntity<>("Currency: " + exception.GetCurrencyTickerCode() + " cannot be found", HttpStatus.NOT_FOUND);
    }
}