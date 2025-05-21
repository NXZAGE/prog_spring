package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;

/**
 * Интерфейс поля ввода
 * <p> Выполняет парсинг объекта из строки </p>
 * <p> Предоставляет подсказки для ввода </p>
 */
public interface Field <T> {
    /**
     * @return подсказка для ввода строки, которую нужно будет распарсить
     */
    public String getPrompt();
    /**
     * Парсит строку в объект
     * @param line строка, которую нужно распарсить
     * @return итоговый объект
     * @throws ParseException если формат не совпадает
     */
    public T parse(String line) throws ParseException;
}
