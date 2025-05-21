package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

public interface AutoValidatableField <T> extends Field<T> {
    /**
     * Парсит строку в объект и сразу выполняет валидацию
     * @param line строка, которую нужно распарсить
     * @return итоговый объект
     * @throws ParseException если формат не совпадает
     * @throws ValidationException если данные не валидны
     */
    public T parseWithValidation(String line) throws ParseException;
}
