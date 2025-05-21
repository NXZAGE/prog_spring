package com.itmo.nxzage.common.util.serialization;

import java.util.ArrayList;
import java.util.Collection;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;

public class PersonConverter implements CSVConverter<Person> {

    @Override
    public Collection<Person> deserialize(String data) throws CSVParseException {
        Collection<Person> result = new ArrayList<Person>();
        Person prototype = new Person();
        for (String line : data.split(CSVConvertable.STRING_DELIMETER)) {
            result.add(prototype.deserializeCSV(line));
        }
        return result;
    }

    @Override
    public String serialize(Collection<Person> collection) {
        StringBuilder result = new StringBuilder();
        for (Person element : collection) {
            result.append(element.serializeCSV());
            result.append(CSVConvertable.STRING_DELIMETER);
        }
        return result.toString();
    }
}
