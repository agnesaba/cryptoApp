package com.test.cryptocurrency.repositories;

import com.test.cryptocurrency.entities.CurrencyEntity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Test
    public void whenFindByTicker_thenReturnCurrency() {
        // when
        CurrencyEntity currencyEntity = currencyRepository.findOneByTicker("BTC");

        // then
        assertThat(currencyEntity.getName()).isEqualTo("Bitcoin");
    }

    @Test
    public void whenFindAll_thenReturnInitialCurrencyList() {
        // when
        Iterable<CurrencyEntity> products = currencyRepository.findAll();

        // then
        assertThat(products).hasSize(4);
    }
}