package com.itmo.nxzage.server;

import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.exceptions.ValidationException;
import com.itmo.nxzage.server.commands.AddCommand;
import com.itmo.nxzage.server.commands.AddIfMaxCommand;
import com.itmo.nxzage.server.commands.AddIfMinCommand;
import com.itmo.nxzage.server.commands.ClearCommand;
import com.itmo.nxzage.server.commands.FilterPassportIDPrefixCommand;
import com.itmo.nxzage.server.commands.GetAllCommand;
import com.itmo.nxzage.server.commands.GetNationalitiesAscCommand;
import com.itmo.nxzage.server.commands.GetNationalitiesDescCommand;
import com.itmo.nxzage.server.commands.InfoCommand;
import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.commands.RemoveByID;
import com.itmo.nxzage.server.commands.RemoveLowerCommand;
import com.itmo.nxzage.server.commands.SaveCommand;
import com.itmo.nxzage.server.commands.UpdateCommand;
import com.itmo.nxzage.server.exceptions.CommandRecognitionException;

public class CommandRecognizer {
    private static String parseName(DataContainer command) throws CommandRecognitionException {
        try {
            command.assertType("name", String.class);
        } catch (ValidationException e) {
            throw new CommandRecognitionException("Unable to parse name");
        }

        return command.get("name", String.class).trim();
    }

    private static Integer parseID(DataContainer command) throws CommandRecognitionException {
        try {
            command.assertType("id", Integer.class);
        } catch (ValidationException e) {
            throw new CommandRecognitionException("Unable to parse id");
        }

        return command.get("id", Integer.class);
    }

    private static Person parsePerson(DataContainer command) throws CommandRecognitionException {
        try {
            command.assertType("person", Person.class);
        } catch (ValidationException e) {
            throw new CommandRecognitionException("Unable to parse name");
        }

        return command.get("person", Person.class);
    }

    private static String parsePassportPrefix(DataContainer command)
            throws CommandRecognitionException {
        try {
            command.assertType("passport_prefix", String.class);
        } catch (ValidationException e) {
            throw new CommandRecognitionException("Unable to parse name");
        }

        return command.get("passport_prefix", String.class);
    }

    public PersonStorageCommand recognize(DataContainer command)
            throws CommandRecognitionException {
        String name = parseName(command);
        return switch (name) {
            case "info" -> new InfoCommand();
            case "show" -> new GetAllCommand();
            case "print_field_ascending_nationality" -> new GetNationalitiesAscCommand();
            case "print_field_descending_nationality" -> new GetNationalitiesDescCommand();
            case "filter_starts_with_passport_id" -> new FilterPassportIDPrefixCommand(
                    parsePassportPrefix(command));
            case "add" -> new AddCommand(parsePerson(command));
            case "add_if_max" -> new AddIfMaxCommand(parsePerson(command));
            case "add_if_min" -> new AddIfMinCommand(parsePerson(command));
            case "update" -> new UpdateCommand(parseID(command), parsePerson(command));
            case "remove_by_id" -> new RemoveByID(parseID(command));
            case "remove_lower" -> new RemoveLowerCommand(parsePerson(command));
            case "clear" -> new ClearCommand();
            case "save" -> new SaveCommand();
            default -> throw new CommandRecognitionException("Unknown command: " + name);
        };
    }
}
