package com.itmo.nxzage.common.util.data;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;
import com.itmo.nxzage.common.util.exceptions.ValidationException;
import com.itmo.nxzage.common.util.serialization.CSVConvertable;

public final class Location implements Validatable, CSVConvertable<Location>, Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DEFAULT_NAME = "noname";
    private static final Integer NAME_MAX_LENGTH = 210;
    // private static final String X_NULL_MESSAGE;
    // private static final String NAME_NULL_MESSAGE;
    // private static final String NAME_LENGTH_MESSAGE;
    // private static final String NAME_CONTAINS_CSV_DELIMETER_MESSAGE;
    // private static final String NAME_CONTAINS_CSV_STRING_DELIMETER_MESSAGE;
    private static final String CSV_DESERIALIZATION_PATTERN;

    private Float x; // can't be null
    private Integer y;
    private Long z;
    private String name; // can't be null, can't be longer than 210

    static {
        // X_NULL_MESSAGE = "x can\'t be null.";
        // NAME_NULL_MESSAGE = "name can\'t be null.";
        // NAME_LENGTH_MESSAGE = "name must be no longer than 210";
        // NAME_CONTAINS_CSV_DELIMETER_MESSAGE = "name can\'t contain CSV delimeter";
        // NAME_CONTAINS_CSV_STRING_DELIMETER_MESSAGE = "name can\'t contain CSV string delimeter";
        String floatPattern = "\\d+(?:\\.\\d+)?";
        String intPattern = "\\d+";
        CSV_DESERIALIZATION_PATTERN = String.format("^Location\\((%s),(%s),(%s)\\)#(.+)$",
                floatPattern, intPattern, intPattern);
    }

    public Location() {
        this.x = 0f;
        this.y = 0;
        this.z = 0L;
        this.name = DEFAULT_NAME;
    }

    public Location(Float x, Integer y, Long z, String name) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setName(name);
    }

    public Location deserializeCSV(String code) throws CSVParseException {
        Pattern pattern = Pattern.compile(CSV_DESERIALIZATION_PATTERN);
        Matcher matcher = pattern.matcher(code);
        if (matcher.find()) {
            try {
                Float x = Float.parseFloat(matcher.group(1));
                Integer y = Integer.parseInt(matcher.group(2));
                Long z = Long.parseLong(matcher.group(3));
                String name = matcher.group(4);
                return new Location(x, y, z, name);
            } catch (NumberFormatException exc) {
                throw new CSVParseException("Parsing Failed: " + exc.getMessage(), exc);
            }
        }
        throw new CSVParseException("CSV string doesn\'t match the pattern");
    }

    public Float getX() {
        return this.x;
    }

    public Integer getY() {
        return this.y;
    }

    public Long getZ() {
        return this.z;
    }

    public String getName() {
        return this.name;
    }

    public void setX(Float value) {
        this.x = value;
    }

    public void setY(Integer value) {
        this.y = value;
    }

    public void setZ(Long value) {
        this.z = value;
    }

    @Override
    public void validate() throws ValidationException {
        if (x == null) {
            throw new ValidationException("X can\'t be null");
        }
        if (name == null) {
            throw new ValidationException("Name can\'t be null");
        }
        if (name.length() > NAME_MAX_LENGTH) {
            throw new ValidationException(
                    String.format("Name can\'t be longer than %d symbols", NAME_MAX_LENGTH));
        }
        if (name.contains(CSVConvertable.DELIMETER)) {
            throw new ValidationException(
                    String.format("Name can\'t contain \'%s\' sumbol", CSVConvertable.DELIMETER));
        }
        if (name.contains(CSVConvertable.STRING_DELIMETER)) {
            throw new ValidationException(String.format("Name can\'t contain \'%s\' sumbol",
                    CSVConvertable.STRING_DELIMETER));
        }
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override
    public String serializeCSV() {
        String pattern = "Location(%f,%d,%d)#%s";
        return String.format(pattern, x, y, z, name);
    }

    @Override
    public String toString() {
        String result =
                String.format("Location[\"%s\"](%f, %d, %d)", this.name, this.x, this.y, this.z);
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Location location = (Location) other;
        return (this.x.equals(location.x) && this.y.equals(location.y)
                && this.z.equals(location.z));
    }

    @Override
    public int hashCode() {
        final int mod = 31;
        int hash = mod;
        hash = hash * mod + x.hashCode();
        hash = hash * mod + y.hashCode();
        hash = hash * mod + z.hashCode();
        return hash;
    }
}
