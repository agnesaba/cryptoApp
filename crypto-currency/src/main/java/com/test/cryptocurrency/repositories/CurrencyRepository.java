package com.test.cryptocurrency.repositories;

import com.test.cryptocurrency.entities.CurrencyEntity;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface CurrencyRepository extends PagingAndSortingRepository<CurrencyEntity, String> {

    public CurrencyEntity findOneByTicker(String ticker);

    public void deleteByTicker(String ticker);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Currency c SET c.name = :name, c.numberOfCoins = :numberOfCoins, c.marketCap = :marketCap WHERE c.ticker = :ticker")
    public void updateCurrency(@Param("name") String name, @Param("numberOfCoins") long numberOfCoins,
     @Param("marketCap") double marketCap, @Param("ticker") String ticker);
}