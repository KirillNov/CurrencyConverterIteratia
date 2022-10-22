package com.iteratia.novikov.com.currencyconverter.services.Impl;

import com.iteratia.novikov.com.currencyconverter.models.OperationsHistory;
import com.iteratia.novikov.com.currencyconverter.repositories.OperationsHistoryRepository;
import com.iteratia.novikov.com.currencyconverter.services.OperationsHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Сервис для валют работающий с БД
 */

@Service
@Slf4j
public class OperationsHistoryServiceImpl implements OperationsHistoryService {

    private final OperationsHistoryRepository operationsHistoryRepository;

    @Autowired
    public OperationsHistoryServiceImpl(OperationsHistoryRepository operationsHistoryRepository) {
        this.operationsHistoryRepository = operationsHistoryRepository;
    }

    @Override
    public List<OperationsHistory> findAll() {
        log.info("Получение всей истори операций");
        return operationsHistoryRepository.findAll();
    }

    @Override
    public OperationsHistory save(OperationsHistory operationsHistory) {
        log.info("Сохранение истории операции в БД");
        return operationsHistoryRepository.save(operationsHistory);
    }

    /**
     * Метод который получает список валют по двум буквенным обозначениям, за неделю с отсчетом от данной даты
     */
    @Override
    public List<OperationsHistory> getOperationsHistoryByCharCodesOnWeek(String charCodeFrom, String charCodeTo) {
        String[] charCodeFromSplit = charCodeFrom.split(" ");
        String[] charCodeToSplit = charCodeTo.split(" ");
        log.info("Получение истории операций по двум буквенным значениям {}/{}", charCodeFrom, charCodeTo);
        List<OperationsHistory> operationsHistoryList = operationsHistoryRepository.getAllByCharCodeFromAndCharCodeTo(charCodeFromSplit[0], charCodeToSplit[0]);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date = LocalDate.now();
        return operationsHistoryList.stream()
                .filter(operation -> {
                    LocalDate newDate = LocalDate.parse(operation.getDate(), formatter);
                    return newDate.isAfter(date.minus(8, ChronoUnit.DAYS));
                })
                .collect(Collectors.toList());
    }
}
