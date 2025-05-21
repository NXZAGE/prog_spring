package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.common.util.data.Coordinates;

public class CoordinatesField implements Field<Coordinates> {
    private static Pattern PATTERN;
    private String prompt;

    static {
        // double float
        String floatPattern = "\\-?\\d+(?:\\.\\d+)?";
        PATTERN = Pattern.compile(String.format("(%s)\\s+(%s)", floatPattern, floatPattern));
    }

    public CoordinatesField() {
        this(null);
    }

    public CoordinatesField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Coordinates parse(String line) throws ParseException {
        Matcher matcher;
        if ((matcher = PATTERN.matcher(line.trim())).matches()) {
            Double x = new DoubleField().parse(matcher.group(1));
            Float y = new FloatField().parse(matcher.group(2));
            return new Coordinates(x, y);
        }
        throw new ParseException("Unable to parse Coordinates", 0);
    }
    
}
