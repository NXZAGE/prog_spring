package com.itmo.nxzage.common.util.serialization;

import java.util.Collection;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;

public interface CSVConverter<T extends CSVConvertable<T>> {
    public String serialize(Collection<T> collection);
    public Collection<T> deserialize(String data) throws CSVParseException;
}
