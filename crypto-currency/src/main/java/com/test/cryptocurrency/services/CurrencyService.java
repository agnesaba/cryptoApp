package com.test.cryptocurrency.services;

import java.util.ArrayList;
import java.util.List;

import com.test.cryptocurrency.entities.CurrencyEntity;
import com.test.cryptocurrency.repositories.CurrencyRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javassist.NotFoundException;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository repository;

    public List<CurrencyEntity> getAllCurrencies(final Integer pageNo, final Integer pageSize, final String sortBy) {
        final Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        final Page<CurrencyEntity> pagedResult = repository.findAll(paging);

        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<CurrencyEntity>();
        }
    }

    public CurrencyEntity createCurrency(final CurrencyEntity currency) throws Exception {
        final CurrencyEntity existingCurrency = repository.findOneByTicker(currency.getTicker());
        if (existingCurrency != null) {
            throw new Exception(currency.getTicker() + "Already exists");
        } else {
            final CurrencyEntity.CurrencyBuilder currencyBuilder = new CurrencyEntity.CurrencyBuilder(currency.getName(),
                    currency.getTicker(), currency.getNumberOfCoins(), currency.getMarketCap());
            final CurrencyEntity currencyEntity = currencyBuilder.buildCurrency();

            repository.save(currencyEntity);
            return currencyEntity;
        }
    }

    public CurrencyEntity getCurrencyByTicker(final String ticker) {
		return repository.findOneByTicker(ticker);
	}

	public CurrencyEntity updateCurrency(String ticker, CurrencyEntity currency) throws NotFoundException {
        final CurrencyEntity existingCurrency = repository.findOneByTicker(ticker);

        if (existingCurrency != null) {
            repository.updateCurrency(currency.getName(), currency.getNumberOfCoins(), currency.getMarketCap(), ticker);
            return currency;
        } else {
            throw new NotFoundException(ticker);
        }
	}

	public void deleteCurrency(String ticker) throws NotFoundException {
        final CurrencyEntity existingCurrency = repository.findOneByTicker(ticker);

        if (existingCurrency != null) {
            repository.deleteByTicker(ticker);
        } else {
            throw new NotFoundException(ticker);
        }
        
	}

}