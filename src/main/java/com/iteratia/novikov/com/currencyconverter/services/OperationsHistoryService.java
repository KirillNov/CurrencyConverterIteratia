package com.iteratia.novikov.com.currencyconverter.services;

import com.iteratia.novikov.com.currencyconverter.models.Currency;
import com.iteratia.novikov.com.currencyconverter.models.OperationsHistory;

import java.util.List;

public interface OperationsHistoryService {

    List<OperationsHistory> findAll();

    OperationsHistory save(OperationsHistory operationsHistory);

    List<OperationsHistory> getOperationsHistoryByCharCodesOnWeek(String charCode1, String charCode2);


}
