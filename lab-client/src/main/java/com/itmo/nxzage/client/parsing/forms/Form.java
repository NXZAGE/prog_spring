package com.itmo.nxzage.client.parsing.forms;

import java.text.ParseException;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

@FunctionalInterface
public interface Form <T> {
    /**
     * Метод заполнения формы из источника ввода
     * @param in input manager
     * @param out output handler
     * @return объект, считанный формой
     * @throws ParseException если фолрмат ввода не верен
     * @throws ValidationException если данные не валидны
     */
    public T fill(InputManager in, OutputHandler out) throws ParseException;
}
