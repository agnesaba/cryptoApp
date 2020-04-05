package com.test.cryptocurrency.controllers;

import com.test.cryptocurrency.entities.CurrencyEntity;
import com.test.cryptocurrency.exceptionHandlers.CurrencyCannotBeCreatedException;
import com.test.cryptocurrency.exceptionHandlers.CurrencyNotFoundException;
import com.test.cryptocurrency.services.CurrencyService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.*;

import javassist.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class CurrencyControllerTest {
    @InjectMocks
    CurrencyController currencyController;

    @Mock
    CurrencyService currencyService;

    @Test
    public void CreateCurrency_addsNewCurrency() throws Exception {
        // given
        final CurrencyEntity currencyEntity = new CurrencyEntity.CurrencyBuilder("ABC", "TestCoin", 16670000,
                69020000000.00).buildCurrency();

        when(currencyService.createCurrency(any(CurrencyEntity.class))).thenReturn(currencyEntity);

        // when
        final ResponseEntity<CurrencyEntity> responseEntity = currencyController.CreateCurrency(currencyEntity);

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 201);
        assertEquals(responseEntity.getBody().getTicker(), currencyEntity.getTicker());
    }

    @Test(expected = CurrencyCannotBeCreatedException.class)
    public void CreateCurrency_doesNotAddACurrencyWithoutATickerCode() throws Exception {
        // given
        final CurrencyEntity currencyEntity = new CurrencyEntity.CurrencyBuilder("ABC", "TestCoin", 16670000,
                69020000000.00).buildCurrency();

        when(currencyService.createCurrency(any(CurrencyEntity.class))).thenThrow(new Exception());

        // when
        final ResponseEntity<CurrencyEntity> responseEntity = currencyController.CreateCurrency(currencyEntity);

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 409);
    }

    @Test
    public void GetAllCurrencies_returnsAllExistingCurrencies() {
        // given
        CurrencyEntity currency1 = new CurrencyEntity.CurrencyBuilder("ABC", "TestCoin", 16670000, 69020000000.00)
                .buildCurrency();
        CurrencyEntity currency2 = new CurrencyEntity.CurrencyBuilder("BBB", "TestCoin2", 16670200, 69010000000.00)
                .buildCurrency();
        List<CurrencyEntity> currencies = new ArrayList<>();
        currencies.addAll(Arrays.asList(currency1, currency2));

        when(currencyService.getAllCurrencies(anyInt(), anyInt(), anyString())).thenReturn(currencies);

        // when
        ResponseEntity<List<CurrencyEntity>> result = currencyController.GetAllCurrencies(0, 10, "a");

        // then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody().get(0).getTicker(), currency1.getTicker());
        assertEquals(result.getBody().get(1).getTicker(), currency2.getTicker());
    }

    @Test
    public void GetAllCurrencies_returnsAnEmptyArrayWhenThereAreNoCurrencies() {
        // when
        ResponseEntity<List<CurrencyEntity>> result = currencyController.GetAllCurrencies(0, 10, "a");

        // then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), new ArrayList<>());
    }

    @Test
    public void GetCurrencyByTicker_returnsFoundTicker() {
        // given
        CurrencyEntity currency = new CurrencyEntity.CurrencyBuilder("ABC", "TestCoin", 16670000, 69020000000.00)
                .buildCurrency();

        when(currencyService.getCurrencyByTicker(anyString())).thenReturn(currency);

        // when
        ResponseEntity<CurrencyEntity> result = currencyController.GetCurrencyByTicker("a");

        // then
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody().getTicker(), currency.getTicker());
        assertEquals(result.getBody().getName(), currency.getName());
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void GetCurrencyByTicker_handlesExceptionWhenCurrencyCannotBeFound() throws Exception {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        when(currencyService.createCurrency(any(CurrencyEntity.class))).thenThrow(new Exception());

        // when
        final ResponseEntity<CurrencyEntity> responseEntity = currencyController.GetCurrencyByTicker("ABC");

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 404);
    }

    @Test
    public void UpdateCurrency_updatesAnExisitingCurrency() throws NotFoundException {
        // given
        final MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        final CurrencyEntity currencyEntity = new CurrencyEntity.CurrencyBuilder("ABC", "TestCoin", 16670000,
                69020000000.00).buildCurrency();

        when(currencyService.updateCurrency(anyString(), any(CurrencyEntity.class))).thenReturn(currencyEntity);

        // when
        final ResponseEntity<CurrencyEntity> responseEntity = currencyController.UpdateCurrency("ABC", currencyEntity);

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 201);
        assertEquals(responseEntity.getBody().getTicker(), currencyEntity.getTicker());
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void UpdateCurrency_handlesExceptionWhenCurrencyCannotBeFound() throws NotFoundException {
        // given
        final CurrencyEntity currencyEntity = new CurrencyEntity.CurrencyBuilder("ABC", "TestCoin", 16670000,
                69020000000.00).buildCurrency();
        when(currencyService.updateCurrency(anyString(), any(CurrencyEntity.class)))
                .thenThrow(new NotFoundException("ABC"));

        // when
        final ResponseEntity<CurrencyEntity> responseEntity = currencyController.UpdateCurrency("ABC", currencyEntity);

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 404);
    }

    @Test
    public void DeleteCurrency_deletesAnExisitingCurrency() throws NotFoundException {

        // when
        final ResponseEntity<HttpStatus> responseEntity = currencyController.DeleteCurrency("ABC");

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 204);
    }

    @Test(expected = CurrencyNotFoundException.class)
    public void DeleteCurrency_handlesExceptionWhenCurrencyCannotBeFound() throws NotFoundException {
        // given
        doThrow(new NotFoundException("")).when(currencyService).deleteCurrency(anyString());

        // when
        final ResponseEntity<HttpStatus> responseEntity = currencyController.DeleteCurrency("ABC");

        // then
        assertEquals(responseEntity.getStatusCodeValue(), 404);
    }

}