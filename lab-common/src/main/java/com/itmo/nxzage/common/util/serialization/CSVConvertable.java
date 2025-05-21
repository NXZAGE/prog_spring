package com.itmo.nxzage.common.util.serialization;

import com.itmo.nxzage.common.util.exceptions.CSVParseException;

public interface CSVConvertable<T extends CSVConvertable<T>> {
    public static final String DELIMETER = "$";
    public static final String DELIMETER_ESCAPE = "\\$";
    public static final String STRING_DELIMETER = "\n"; 
    public String serializeCSV();
    public T deserializeCSV(String data) throws CSVParseException;
}
