package com.iteratia.novikov.com.currencyconverter.services.Impl;

import com.iteratia.novikov.com.currencyconverter.models.Parser;
import com.iteratia.novikov.com.currencyconverter.repositories.CurrencyRepository;
import com.iteratia.novikov.com.currencyconverter.models.Currency;
import com.iteratia.novikov.com.currencyconverter.services.CurrencyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для валют работающий с БД
 */

@Service
@Slf4j
public class CurrencyServiceImpl implements CurrencyService {

    Parser parser = new Parser();

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
    /**
     * Метод который служит для заполнения БД, по этому помечен аннотацией @PostConstruct
     * @PostConstruct , вызывается только один раз, сразу после инициализации свойств компонента
     */
    @PostConstruct
    private void addCurrenciesInDB() {
        log.info("Проверка, заполнение и обновление БД валютами");
        List<Currency> currencies =findAll();
        List<Currency> checkList =
                    parser.allCurrencies().stream()
                            .filter(currency -> !currencies.contains(currency)).
                            sorted(Comparator.comparing(Currency::getCharCode)).
                            map(currency -> {
                                for (Currency value : currencies) {
                                    if (!value.getDate().equals(currency.getDate())) {
                                        currencyRepository.delete(value);
                                        currencyRepository.save(currency);
                                    }
                                }
                                return currency;
                            })
                            .collect(Collectors.toList());
            saveAll(checkList);
    }

    @Override
    public List<Currency> findAll() {
        log.info("Получение всех валют");
        return currencyRepository.findAll();
    }

    @Override
    public void saveAll(List<Currency> currencies) {
        log.info("Сохранение всех валют в БД");
        currencyRepository.saveAll(currencies);
    }

    /**
     * Метод который находит валюту по символьному обозначению
     */
    @Override
    public Currency getByCharCode(String charCode) {
        log.info("Получение валюты по буквенному обозначению {}", charCode);
        String[] srt = charCode.split(" ");
        return currencyRepository.findByCharCode(srt[0]).orElseThrow(() -> new RuntimeException("Ошибка. Невозможно найти валюту "
                + "'" + charCode + "'"));
    }
    /**
     * Метод который получает дату с сайта ЦБ РФ
     */
    @Override
    public String getDate() {
        log.info("Запрос даты с сайта ЦБ РФ");
        Node date = parser.buildDocument().getElementsByTagName("ValCurs").item(0);
        String dateCB = date.getAttributes().getNamedItem("Date").getNodeValue();
        log.info("Дата: {}", dateCB);
        return dateCB;
    }


}
