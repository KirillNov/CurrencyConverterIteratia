package com.iteratia.novikov.com.currencyconverter.models;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Класс описывающий валюту
 * numCode - номер валюты
 * charCode - Буквенное обозначение валюты
 * nominal - номинальное отношение
 * name - имя валюты
 * value - значение по отношению к рублю
 * date - дата получение валюты из ЦБ РФ
 *
 */


@Data
@Entity
@Table(name = "all_currencies")
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Currency {

    @Id
    @NonNull
    @Column(name = "id")
    private String id;

    @NonNull
    @Column(name = "num_code")
    private Integer numCode;

    @NonNull
    @Column(name = "char_code")
    private String charCode;

    @NonNull
    @Column(name = "nominal")
    private Integer nominal;

    @NonNull
    @Column(name = "namecc")
    private String name;

    @NonNull
    @Column(name = "valuecc")
    private Double value;

    @NonNull
    @Column(name = "datecc")
    private String date;

}
