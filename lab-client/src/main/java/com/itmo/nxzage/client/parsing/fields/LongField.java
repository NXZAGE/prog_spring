package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;

public class LongField implements Field<Long> {
    private String prompt;

    public LongField() {
        this(null);
    }

    public LongField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Long parse(String line) throws ParseException {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(line.trim());
        } catch (NumberFormatException exception) {
            throw new ParseException("Unable to parse Long", 0);
        }
    }
}
