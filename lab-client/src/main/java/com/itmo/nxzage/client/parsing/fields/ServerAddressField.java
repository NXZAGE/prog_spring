package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

public class ServerAddressField implements AutoValidatableField<Map<String, Object>> {
    // private File parent;
    // ? я не ебу как это валидировать
    // TODO файлы имеют абсолютную адресацию и надо пропихивать родителей или забить хуй и работать только с абсолютными путями
    private static Map<String, Object> packToMap(String address) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("address", address);
        return map;
    }


    @Override
    public Map<String, Object> parseWithValidation(String line) throws ParseException {
        Map<String, Object> map = parse(line);
        if (((String) map.get("address")).isBlank()) {
            throw new ValidationException("Attempt to parse null address");
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
            throw new ParseException("Attempt to parse null address", 0);   
        }
        return packToMap(new StringField().parse(line.trim()));
    }
    
}
