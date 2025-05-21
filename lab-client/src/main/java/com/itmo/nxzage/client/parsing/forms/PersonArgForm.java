package com.itmo.nxzage.client.parsing.forms;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;

/**
 * Форма набора стандратных аргументов
 * <p>Содержит поля <b>person: Person</b></p>
 */
public class PersonArgForm implements Form<Map<String, Object>> {
    PersonForm innerForm;

    public PersonArgForm() {
        innerForm = new PersonForm();
    }

    @Override
    public Map<String, Object> fill(InputManager in, OutputHandler out) throws ParseException {
        var map = new HashMap<String, Object>();
        map.put("person", innerForm.fill(in, out));
        return map;
    }
    
}
