package com.test.cryptocurrency.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity(name="Currency")
public class CurrencyEntity {
    @Id
    @Column(unique = true)
    private String name;
    private String ticker;
    private long numberOfCoins;
    private double marketCap;

    private CurrencyEntity(CurrencyBuilder builder) {
        this.name = builder.name;
        this.ticker = builder.ticker;
        this.numberOfCoins = builder.numberOfCoins;
        this.marketCap = builder.marketCap;
    }

    private CurrencyEntity() {}

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    public long getNumberOfCoins() {
        return numberOfCoins;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public static class CurrencyBuilder {
        private String name;
        private String ticker;
        private long numberOfCoins;
        private double marketCap;

        @JsonCreator
        public CurrencyBuilder(@JsonProperty("name") String name, @JsonProperty("ticker") String ticker, 
        @JsonProperty("numberOfCoins") long numberOfCoins, @JsonProperty("marketCap") double marketCap) {
            this.name = name;
            this.ticker = ticker;
            this.numberOfCoins = numberOfCoins;
            this.marketCap = marketCap;
        }

        public CurrencyBuilder setName(String name) {
            this.name = name;
            return this;
        }
        
        public CurrencyBuilder setNumberOfCoins(int numberOfCoins) {
            this.numberOfCoins = numberOfCoins;
            return this;
        }

        public CurrencyBuilder setMarketCap(double marketCap) {
            this.marketCap = marketCap;
            return this;
        }

        public CurrencyEntity buildCurrency() {
            return new CurrencyEntity(this);
        }
        
    }    
}