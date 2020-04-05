package com.test.cryptocurrency.controllers;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.test.cryptocurrency.entities.CurrencyEntity;
import com.test.cryptocurrency.exceptionHandlers.CurrencyCannotBeCreatedException;
import com.test.cryptocurrency.exceptionHandlers.CurrencyNotFoundException;
import com.test.cryptocurrency.services.CurrencyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api/currencies")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Returns a list of all currencies with sorting and pagination
     *
     * @param size the page size
     * @param page the page number
     * @param sort the field to sort the list on
     * @return list of currencies
     */
    @GetMapping
    public ResponseEntity<List<CurrencyEntity>> GetAllCurrencies(@RequestParam(defaultValue = "10") final Integer size,
            @RequestParam(defaultValue = "0") final Integer page,
            @RequestParam(defaultValue = "ticker") final String sort) {
        logger.info("GetAllCurrencies successfully called");
        final List<CurrencyEntity> currencies = currencyService.getAllCurrencies(page, size, sort);
        return new ResponseEntity<List<CurrencyEntity>>(currencies, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * Returns a currency on a given ticker code
     *
     * @param ticker the ticker code
     * @return CurrencyEntity the found currency
     */
    @GetMapping("/{ticker}")
    public ResponseEntity<CurrencyEntity> GetCurrencyByTicker(@PathVariable("ticker") final String ticker) {
        final CurrencyEntity currency = currencyService.getCurrencyByTicker(ticker);

        if (currency != null) {
            logger.info("GetCurrency called to retrieve: " + ticker + " successfully called");
            return new ResponseEntity<>(currency, HttpStatus.OK);
        } else {
            logger.error("GetCurrency called to retrieve: " + ticker + " failed");
            throw new CurrencyNotFoundException(ticker);
        }
    }

    /**
     * Creates a new currency
     *
     * @param currency the currency object
     * @return CurrencyEntity the new currency
     */
    @PostMapping(consumes = "application/json")
    public ResponseEntity<CurrencyEntity> CreateCurrency(@RequestBody final CurrencyEntity currency) {
        try {
            CurrencyEntity createdCurrency = currencyService.createCurrency(currency);
            logger.info("CreateCurrency for: " + createdCurrency.getTicker() + " successfully called");
            return new ResponseEntity<>(createdCurrency, HttpStatus.CREATED);
        } catch (final Exception e) {
            logger.info("CreateCurrency for: " + currency.getTicker() + " failed");
            logger.error(e.getMessage());
            throw new CurrencyCannotBeCreatedException(currency.getTicker());
        }
    }

    /**
     * Updates an exisiting currency
     *
     * @param ticker   the ticker code of the exisiting currency
     * @param currency the new currency details
     * 
     * @return CurrencyEntity the updated currencyEntity object
     */
    @PutMapping("/{ticker}")
    public ResponseEntity<CurrencyEntity> UpdateCurrency(@PathVariable("ticker") final String ticker,
            @RequestBody final CurrencyEntity currency) {
        try {
            CurrencyEntity updatedCurrency = currencyService.updateCurrency(ticker, currency);
            logger.info("UpdateCurrency for: " + ticker + " successfully called");
            return new ResponseEntity<>(updatedCurrency, HttpStatus.CREATED);
        } catch (final NotFoundException e) {
            logger.info("UpdateCurrency for: " + ticker + " failed");
            logger.error(e.getMessage());
            throw new CurrencyNotFoundException(ticker);
        }
    }

    /**
     * Deletes an exisiting currency
     *
     * @param ticker the ticker code of the exisiting currency
     * 
     * @return HttpStatus code
     */
    @DeleteMapping("/{ticker}")
    public ResponseEntity<HttpStatus> DeleteCurrency(@PathVariable("ticker") final String ticker) {
        try {
            currencyService.deleteCurrency(ticker);
            logger.info("DeleteCurrency for: " + ticker + " successfully called");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (final NotFoundException e) {
            logger.info("DeleteCurrency for: " + ticker + " failed");
            logger.error(e.getMessage());
            throw new CurrencyNotFoundException(ticker);
        }
    }

}