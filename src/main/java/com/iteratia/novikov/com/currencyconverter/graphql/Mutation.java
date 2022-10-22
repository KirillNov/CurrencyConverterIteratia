package com.iteratia.novikov.com.currencyconverter.graphql;


import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.iteratia.novikov.com.currencyconverter.models.Currency;
import com.iteratia.novikov.com.currencyconverter.models.OperationsHistory;
import com.iteratia.novikov.com.currencyconverter.services.CurrencyService;
import com.iteratia.novikov.com.currencyconverter.services.OperationsHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Класс разрешающий GraphQL запросы
 * Класс записывает истории операций в БД
 */

@Component
public class Mutation implements GraphQLMutationResolver {

    CurrencyService currencyService;
    OperationsHistoryService operationsHistoryService;

    @Autowired
    public Mutation(CurrencyService currencyService, OperationsHistoryService operationsHistoryService) {
        this.currencyService = currencyService;
        this.operationsHistoryService = operationsHistoryService;
    }

    public OperationsHistory saveOperationsHistory (String charCode1, String charCode2, String sum) {
        Currency currency1 = currencyService.getByCharCode(charCode1);
        Currency currency2 = currencyService.getByCharCode(charCode2);
        return operationsHistoryService.save(new OperationsHistory(currency1, currency2, Double.parseDouble(sum)));
    }

}
