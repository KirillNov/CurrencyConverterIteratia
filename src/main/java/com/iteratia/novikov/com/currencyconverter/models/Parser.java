package com.iteratia.novikov.com.currencyconverter.models;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Класс получающий данные из ЦБ РФ в виде XML и который парсит полученные данные методом DOM
 */
public class Parser {
    private static final String TAG_VALUTE = "Valute";
    private static final String TAG_VALUE = "Value";
    private static final String TAG_NumCode = "NumCode";
    private static final String TAG_CharCode = "CharCode";
    private static final String TAG_Nominal = "Nominal";
    private static final String TAG_Name = "Name";

    /**
     * Метод получени данных из сайта ЦБ РФ и конвертация в объект Document
     */
    @SneakyThrows
    public Document buildDocument(){
        URL url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder().parse(url.openStream());
    }
    /**
     * Получение даты на сегодняшний день
     */
    public String getDataCurrencies() {
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format(date);
    }

    /**
     * Получение списка Валют.
     * Получаем из объекта Document ноды по тегам и извлекаем из них необходимую информацию.
     */
    @SneakyThrows
    public List<Currency> allCurrencies()  {
        Document document = buildDocument();
        String id = "";
        int numCode = 0;
        String charCode = "";
        int nominal = 0;
        String name = "";
        double value = 0.0;

        ArrayList<Currency> currencyList = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName(TAG_VALUTE);
        for (int i = 0; i<nodeList.getLength(); i++) {
            Node currencyNode = nodeList.item(i);
            id = currencyNode.getAttributes().getNamedItem("ID").getNodeValue();
            NodeList childNodes = nodeList.item(i).getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                switch (childNodes.item(j).getNodeName()) {
                    case TAG_NumCode -> numCode = Integer.parseInt(childNodes.item(j).getTextContent());
                    case TAG_CharCode -> charCode = childNodes.item(j).getTextContent();
                    case TAG_Nominal -> nominal = Integer.parseInt(childNodes.item(j).getTextContent());
                    case TAG_Name -> name = childNodes.item(j).getTextContent();
                    case TAG_VALUE -> value = Double.parseDouble(childNodes.item(j).getTextContent().replace(",", "."));
                }
            }
            Currency currency = new Currency(id, numCode, charCode, nominal, name, value, getDataCurrencies());
            currencyList.add(currency);
        }
        return currencyList;
    }
}
