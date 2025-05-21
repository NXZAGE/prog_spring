package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringField implements Field<String> {
    private static Pattern MULTIWORD_PATTERN;
    private static Pattern SINGLEWORD_PATTERN;
    private String prompt;

    static {
        // TODO make more spec simbols
        MULTIWORD_PATTERN = Pattern.compile("\\\"([\\w\\d\\s_\\-\\*\\.\\+\\/\\\\]*)\\\"");
        SINGLEWORD_PATTERN = Pattern.compile("[\\w\\d_\\-\\*\\.\\+\\/\\\\]+");
    }

    public StringField() {
        this(null);
    }

    public StringField(String prompt) {
        this.prompt = prompt;
    }

    @Override 
    public String getPrompt() {
        return prompt;
    }

    @Override
    public String parse(String line) throws ParseException {
        line = line.trim();
        Matcher matcher;
        if ((matcher = MULTIWORD_PATTERN.matcher(line)).matches()) {
            return matcher.group(1);
        }
        if ((matcher = SINGLEWORD_PATTERN.matcher(line)).matches()) {
            return matcher.group();
        }
        throw new ParseException("Unable to parse string", 0);
    }
}
