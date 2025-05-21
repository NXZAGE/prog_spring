package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;

public class FloatField implements Field<Float> {
    private String prompt;

    public FloatField() {
        this(null);
    }

    public FloatField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Float parse(String line) throws ParseException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            return Float.parseFloat(line.trim());
        } catch (NumberFormatException exception) {
            throw new ParseException("Unable to parse Float", 0);
        }
    }
}
