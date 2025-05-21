package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

public class IDField implements AutoValidatableField<Map<String, Object>> {
    private static Map<String, Object> packToMap(Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        return map;
    }

    @Override
    public Map<String, Object> parseWithValidation(String line) throws ParseException {
        Map<String, Object> map = parse(line);
        if (map.get("id") == null) {
            throw new ValidationException("ID can\'t be null");
        }
        if ((Integer) map.get("id") < 0) {
            throw new ValidationException("ID can\'t be negative");
        }
        return map;
    }

    @Override
    public String getPrompt() {
        return "Enter id: ";
    }

    @Override
    public Map<String, Object> parse(String line) throws ParseException {
        if (line == null) {
            return packToMap(null);
        }
        try {
            return packToMap(Integer.parseInt(line.trim()));
        } catch (NumberFormatException exception) {
            throw new ParseException(String.format("Unable to parse number from string \"%s\"", line.trim()), 0);
        }
    }
    
}
