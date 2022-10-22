package com.iteratia.novikov.com.currencyconverter.services;

import com.iteratia.novikov.com.currencyconverter.models.Currency;

import java.util.List;


public interface CurrencyService {

    List<Currency> findAll();


    void saveAll(List<Currency> currencies);

    Currency getByCharCode(String charCode);

    String getDate();

}
