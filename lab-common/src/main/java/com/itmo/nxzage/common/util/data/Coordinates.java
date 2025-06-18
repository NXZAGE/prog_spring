package com.itmo.nxzage.common.util.data;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;
import com.itmo.nxzage.common.util.exceptions.ValidationException;
import com.itmo.nxzage.common.util.serialization.CSVConvertable;

public final class Coordinates implements Validatable, CSVConvertable<Coordinates>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Double MIN_X = -727d;
    // private static final String X_NULL_MESSAGE;
    // private static final String X_OUT_OF_RANGE_MESSAGE;
    // private static final String Y_NULL_MESSAGE;
    private static final String CSV_DESERIALIZATION_PATTERN;

    private Double x; // more than -727, can't be null
    private Float y; // can't be null

    static {
        // X_NULL_MESSAGE = "X can\'t be null.";
        // X_OUT_OF_RANGE_MESSAGE = String.format("X must be more than %f", MIN_X);
        // Y_NULL_MESSAGE = "Y can\'t be null.";
        String floatPattern = "\\d+(?:\\.\\d+)";
        CSV_DESERIALIZATION_PATTERN = String.format(
                "^Coordinates\\((%s),(%s)\\)$", floatPattern, floatPattern);
    }

    public Coordinates() {
        this.x = 0d;
        this.y = 0f;
    }

    public Coordinates(Double x, Float y) {
        this.setX(x);
        this.setY(y);
    }

    public Coordinates deserializeCSV(String code) throws CSVParseException {
        Pattern pattern = Pattern.compile(CSV_DESERIALIZATION_PATTERN);
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            try {
                Double x = Double.parseDouble(matcher.group(1));
                Float y = Float.parseFloat(matcher.group(2));
                return new Coordinates(x, y);
            } catch (NumberFormatException exc) {
                throw new CSVParseException("Parsing failed: " + exc.getMessage(), exc.getCause()); 
            }
        }
        throw new CSVParseException("CSV string doesn\'t match the pattern");
    }

    public Double getX() {
        return this.x;
    }

    public Float getY() {
        return this.y;
    }

    public void setX(Double value) {
        this.x = value;
    }

    public void setY(Float value) {
        this.y = value;
    }

    @Override
    public void validate() throws ValidationException {
        if (x == null) {
            throw new ValidationException("X can\\'t be null");
        }
        if (y == null) {
            throw new ValidationException("Y can\\'t be null");
        }   
        if (x <= MIN_X) {
            throw new ValidationException("X can't be less than" + MIN_X.toString());
        }
    }

    @Override
    public String serializeCSV() {
        String pattern = "Coordinates(%f,%f)";
        return String.format(Locale.US, pattern, x, y);
    }

    @Override
    public String toString() {
        String result = String.format("Coordinates(%f, %f)", this.x, this.y);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        Coordinates coordinates = (Coordinates) other;
        return (this.x.equals(coordinates.x) && this.y.equals(coordinates.y));
    }

    @Override
    public int hashCode() {
        final int mod = 31;
        int hash = mod;
        hash = hash * mod + x.hashCode();
        hash = hash * mod + y.hashCode();
        return hash;
    }
}
