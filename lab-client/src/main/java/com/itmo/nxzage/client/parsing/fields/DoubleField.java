package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;

public class DoubleField implements Field<Double> {
    private String prompt;

    public DoubleField() {
        this(null);
    }

    public DoubleField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Double parse(String line) throws ParseException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(line.trim());
        } catch (NumberFormatException exception) {
            throw new ParseException("Unable to parse Double", 0);
        }
    }
}
