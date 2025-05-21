package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

public class PassportPrefixField implements AutoValidatableField<Map<String, Object>>  {

    private static Map<String, Object> packToMap(String filename) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("passport_prefix", filename);
        return map;
    }


    @Override
    public Map<String, Object> parseWithValidation(String line) throws ParseException {
        Map<String, Object> map = parse(line);
        if (((String) map.get("passport_prefix")).isBlank()) {
            throw new ValidationException("Attempt to parse null prefix");
        }
        return map;
    }

    @Override
    public String getPrompt() {
        return null;
    }

    @Override
    public Map<String, Object> parse(String line) throws ParseException {
        if (line == null || line.isBlank()) {
            throw new ParseException("Attempt to parse null prefix", 0);   
        }
        return packToMap(new StringField().parse(line.trim()));
    }
    
}
