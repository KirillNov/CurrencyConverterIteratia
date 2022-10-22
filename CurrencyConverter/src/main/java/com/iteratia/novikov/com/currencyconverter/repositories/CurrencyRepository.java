package com.iteratia.novikov.com.currencyconverter.repositories;

import com.iteratia.novikov.com.currencyconverter.models.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для Валют
 */
public interface CurrencyRepository extends JpaRepository<Currency, String> {

    /**
     * Кастомный метод для получения валюты по символьному обозначению
     */
    Optional<Currency> findByCharCode(String charCode);

    }