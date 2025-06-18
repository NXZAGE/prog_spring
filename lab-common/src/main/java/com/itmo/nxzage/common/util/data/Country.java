package com.itmo.nxzage.common.util.data;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;
import com.itmo.nxzage.common.util.serialization.CSVConvertable;

public enum Country implements CSVConvertable<Country> {
    UNITED_KINGDOM(1, "The United Kingdom of Great Britan and Northen Irland"),
    USA(2, "The United States of America"),
    FRANCE(3, "France"),
    SPAIN(4, "Spain"),
    JAPAN(5, "Japanese Empire");

    private String title;
    private Integer id;

    private Country(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Country deserializeCSV(String code) throws CSVParseException {
        // TODO переписать на формы??
        Pattern pattern = Pattern.compile("^Country#(\\d+)$");
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()){
            try {
                Integer id = Integer.parseInt(matcher.group(1));
                for (Country country : Country.class.getEnumConstants()) {
                    if (country.id.equals(id)) {
                        return country;
                    }
                } 
            } catch (NumberFormatException exc) {
                throw new CSVParseException("Parsing failed: " + exc.getMessage(), exc.getCause());   
            }
            throw new IllegalArgumentException(String.format("No such countryID (%d)", id));
        }
        throw new CSVParseException("CSV string doesn\'t match the pattern");
    }

    public String getTitle() {
        return this.title;
    }

    public Integer getID() {
        return this.id;
    }

    @Override
    public String serializeCSV() {
        String pattern = "Country#%d";
        return String.format(Locale.US, pattern, this.id);
    }

    @Override
    public String toString() {
        return this.title;
    }
}
