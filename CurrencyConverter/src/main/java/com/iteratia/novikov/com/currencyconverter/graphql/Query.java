package com.iteratia.novikov.com.currencyconverter.graphql;


import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.iteratia.novikov.com.currencyconverter.models.Currency;
import com.iteratia.novikov.com.currencyconverter.models.OperationsHistory;
import com.iteratia.novikov.com.currencyconverter.models.Stats;
import com.iteratia.novikov.com.currencyconverter.services.CurrencyService;
import com.iteratia.novikov.com.currencyconverter.services.OperationsHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Класс разрешающий GraphQL запросы
 * Возвращающий из БД все валюты и истории операций
 * Так же возвращает валюты по их символьному обозначению
 * Получение статистики по паре валют
 */

@Component
@Slf4j
public class Query implements GraphQLQueryResolver {

    CurrencyService currencyService;
    OperationsHistoryService operationsHistoryService;;



    @Autowired
    public Query(CurrencyService currencyService, OperationsHistoryService operationsHistoryService) {
        this.currencyService = currencyService;
        this.operationsHistoryService = operationsHistoryService;
    }


    public List<Currency> getCurrencies() {
        return currencyService.findAll();
    }

    public Currency getCurrency(String charCode) {
        return currencyService.getByCharCode(charCode);
    }

    public List<OperationsHistory> getOperationsHistory() {
        return operationsHistoryService.findAll();
    }

    public String getDate() {
        return currencyService.getDate();
    }

    public Stats getStats(String charCodeFrom, String charCodeTo) {
        log.info("Получение статистики по двум валютам {}/{}", charCodeFrom, charCodeTo);
        List<OperationsHistory> operationsHistoryList = operationsHistoryService.getOperationsHistoryByCharCodesOnWeek(charCodeFrom, charCodeTo);
        return new Stats(operationsHistoryList);
    }

}
