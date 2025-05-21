package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import com.itmo.nxzage.common.util.data.Country;

public class CountryField implements Field<Country> {
    private String prompt;

    public CountryField() {
        this(null);
    }

    public CountryField(String prompt) {
        this.prompt = prompt;
    }

    @Override
    public String getPrompt() {
        return prompt;
    }

    @Override
    public Country parse(String line) throws ParseException {
        if (line == null || line.isBlank()) {
            throw new ParseException("Country can\'t be null", 0);
        }
        line = line.trim();
        switch (line) {
            case "UNITED_KINGDOM": return Country.UNITED_KINGDOM;
            case "USA": return Country.USA;
            case "FRANCE": return Country.FRANCE;
            case "SPAIN": return Country.SPAIN;
            case "JAPAN": return Country.JAPAN;
            default: throw new ParseException("Unable to parse Country", 0);
        }
    }
}

/*
 * UNITED_KINGDOM
    USA
    FRANCE
    SPAIN;
    JAPAN;
 */
