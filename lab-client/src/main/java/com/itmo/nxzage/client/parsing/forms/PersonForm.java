package com.itmo.nxzage.client.parsing.forms;

import java.text.ParseException;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.client.parsing.fields.CoordinatesField;
import com.itmo.nxzage.client.parsing.fields.CountryField;
import com.itmo.nxzage.client.parsing.fields.Field;
import com.itmo.nxzage.client.parsing.fields.FloatField;
import com.itmo.nxzage.client.parsing.fields.LocationField;
import com.itmo.nxzage.client.parsing.fields.LongField;
import com.itmo.nxzage.client.parsing.fields.StringField;
import com.itmo.nxzage.common.util.data.Coordinates;
import com.itmo.nxzage.common.util.data.Country;
import com.itmo.nxzage.common.util.data.Location;
import com.itmo.nxzage.common.util.data.Person;

/**
 * Форма распознавания Person
 */
public class PersonForm implements Form<Person> {
    private boolean interactive;
    private Person result;

    private <T> T fillField(Field<T> field, InputManager in, OutputHandler out)
            throws ParseException {
        if (interactive) {
            out.printPrompt(field.getPrompt());
        }
        try {
            return field.parse(in.nextLine());
        } catch (ParseException exception) {
            if (interactive) {
                out.printError(exception.getMessage() + "\n");
                return fillField(field, in, out);
            }
            throw exception;
        }
    }

    private void validate() {
        result.validate();
    }

    @Override
    public Person fill(InputManager in, OutputHandler out) throws ParseException {
        interactive = in.isInteractive();
        String name = fillField(new StringField("Enter name (string): "), in, out);
        Coordinates coordinates =
                fillField(new CoordinatesField("Enter coordinates (format:x y):"), in, out);
        Float height = fillField(new FloatField("Enter height: "), in, out);
        Long weight = fillField(new LongField("Enter weight: "), in, out);
        String passportID =
                fillField(new StringField("Enter passportID (string): "), in, out);
        Country nationality = fillField(new CountryField("Eneter country: "), in, out);
        Location location =
                fillField(new LocationField("Enter location (format:x y z name): "), in, out);

        result = new Person(name, coordinates, height, weight, passportID, nationality, location);
        validate();
        return result;
    }
}
