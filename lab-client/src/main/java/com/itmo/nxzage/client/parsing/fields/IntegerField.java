package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;

public class IntegerField implements Field<Integer> {
    private String prompt;

    public IntegerField() {
        this(null);
    }

    public IntegerField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Integer parse(String line) throws ParseException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException exception) {
            throw new ParseException("Unable to parse Integer", 0);
        }
    }
}
