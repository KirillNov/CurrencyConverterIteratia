package com.iteratia.novikov.com.currencyconverter.repositories;

import com.iteratia.novikov.com.currencyconverter.models.OperationsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Репозиторий для истории операций
 */
public interface OperationsHistoryRepository extends JpaRepository<OperationsHistory, Integer> {

    /**
     * Кастомный метод для получения всех операций из БД по двум символьным обозначениям
     */
    List<OperationsHistory> getAllByCharCodeFromAndCharCodeTo(String charCodeFrom, String charCodeTo);
}
