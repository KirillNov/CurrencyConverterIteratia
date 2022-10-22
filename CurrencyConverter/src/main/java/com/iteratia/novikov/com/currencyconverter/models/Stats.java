package com.iteratia.novikov.com.currencyconverter.models;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.List;
/**
 * Класс подсчитывающий статистику
 */
@Data
@Slf4j
public class Stats {

    private List<OperationsHistory> operations;

    private Double avgRate;

    private Double sumValue;

    public Stats(List<OperationsHistory> operations) {
        this.operations = operations;
        this.avgRate = calculateAvgRate(operations);
        this.sumValue = calculateSumValue(operations);
    }

    /**
     * Получение среднего курса и форматирование количества символов после запятой
     */
    private Double calculateAvgRate(List<OperationsHistory> operations) {
        Double avgRate =
        operations.stream()
                .mapToDouble(OperationsHistory::getConversionRate)
                .average()
                .orElse(0);
        String result = new DecimalFormat("#.###").format(avgRate).replace(",", ".");
        log.info("Средний курс конвертации {}", avgRate);
        return Double.valueOf(result);
    }
    /**
     * Получение общего объема сконвертированных валют
     */
    private Double calculateSumValue(List<OperationsHistory> operations) {
        Double sumValue =
         operations.stream()
                .mapToDouble(OperationsHistory::getExchangeSum)
                .sum();
        log.info("Объем конвертации {}", sumValue);
        return sumValue;
    }
}
