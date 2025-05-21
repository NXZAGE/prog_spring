package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.common.util.data.Location;

public class LocationField implements Field<Location> {
    public static Pattern PATTERN;
    private String prompt;

    static {
        // F INT LONG NAME
        String floatPattern = "\\-?\\d+(?:\\.\\d+)?";
        PATTERN = Pattern.compile(String.format("(%s)\\s+(\\-?\\d+)\\s+(\\-?\\d+)\\s+(\\S.*)", floatPattern));
    }

    public LocationField() {
        this(null);
    }

    public LocationField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Location parse(String line) throws ParseException {
        Matcher matcher;
        if ((matcher = PATTERN.matcher(line.trim())).matches()) {
            Float x = new FloatField().parse(matcher.group(1));
            Integer y = new IntegerField().parse(matcher.group(2));
            Long z = new LongField().parse(matcher.group(3));
            String name = new StringField().parse(matcher.group(4));
            return new Location(x, y, z, name);
        } 
        throw new ParseException("Unable to parse Location", 0);
    }
    
}
