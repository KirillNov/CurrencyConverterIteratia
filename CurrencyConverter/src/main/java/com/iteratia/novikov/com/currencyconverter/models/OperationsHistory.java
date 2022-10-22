package com.iteratia.novikov.com.currencyconverter.models;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * Класс описывающий историю операций
 * charCodeFrom - Буквенное обозначение валюты и сама валюты из которой будет конвертация
 * charCodeTo - Буквенное обозначение валюты и сама валюты в которую будет конвертация
 * exchangeSum - входящие данные для конвертации
 * resultExchange - результат конвертации
 * conversionRate - средний курс
 * date - дата проведения конвертации
 */

@Data
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "operations_history")
public class OperationsHistory {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Column(name = "char_code_from")
    private String charCodeFrom;

    @NonNull
    @Column(name = "char_code_to")
    private String charCodeTo;

    @NonNull
    @Column(name = "exchange_sum")
    private Double exchangeSum;

    @NonNull
    @Column(name = "result_exchange")
    private Double resultExchange;

    @NonNull
    @Column(name = "conversion_rate")
    private Double conversionRate;

    @NonNull
    @Column(name = "value_from")
    private Double valueFrom;

    @NonNull
    @Column(name = "value_to")
    private Double valueTo;

    @NonNull
    @Column(name = "date")
    private String date;

    public OperationsHistory(Currency currency1, Currency currency2, double exchangeSum) {
        Parser parser = new Parser();
        this.date = parser.getDataCurrencies();
        this.charCodeFrom = currency1.getCharCode();
        this.charCodeTo = currency2.getCharCode();
        this.conversionRate =conv((currency1.getValue() / currency1.getNominal()) /
                (currency2.getValue() / currency2.getNominal()));
        this.exchangeSum = exchangeSum;
        this.valueFrom = currency1.getValue();
        this.valueTo = currency2.getValue();
        this.resultExchange = conv(conversionRate * this.exchangeSum);
    }

    private double conv (double value) {
        String result = new DecimalFormat("#.###").format(value).replace(",", ".");
        return Double.parseDouble(result);
    }

}
