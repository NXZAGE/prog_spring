package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class ServerPortField implements AutoValidatableField<Map<String, Object>> {
    // private File parent;
    // ? я не ебу как это валидировать
    // TODO файлы имеют абсолютную адресацию и надо пропихивать родителей или забить хуй и работать только с абсолютными путями
    private static Map<String, Object> packToMap(Integer port) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("hostport", port);
        return map;
    }


    @Override
    public Map<String, Object> parseWithValidation(String line) throws ParseException {
        Map<String, Object> map = parse(line);
        return map;
    }

    @Override
    public String getPrompt() {
        return null;
    }

    @Override
    public Map<String, Object> parse(String line) throws ParseException {
        if (line == null || line.isBlank()) {
            throw new ParseException("Attempt to parse null port", 0);   
        }
        return packToMap(new IntegerField().parse(line.trim()));
    }
    
}

