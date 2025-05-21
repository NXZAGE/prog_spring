package com.itmo.nxzage.server;

import java.util.Map;
import com.itmo.nxzage.common.util.data.Person;
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
import com.itmo.nxzage.server.exceptions.CommandDeserializeException;

/**
 * Преобразует объект в формате map в команду
 */
public class CommandDeserializer {
    private static String parseName(Map<String, Object> command)
            throws CommandDeserializeException {
        if (!command.containsKey("name")) {
            throw new CommandDeserializeException("Unable to parse name");
        }
        try {
            return ((String) command.get("name")).trim();
        } catch (ClassCastException exc) {
            throw new CommandDeserializeException("Unable to parse name");
        }
    }

    private static Integer parseID(Map<String, Object> command) throws CommandDeserializeException {
        if (!command.containsKey("id")) {
            throw new CommandDeserializeException("Unable to parse id");
        }
        try {
            return (Integer) command.get("id");
        } catch (ClassCastException exc) {
            throw new CommandDeserializeException("Unable to parse id");
        }
    }

    private static Person parsePerson(Map<String, Object> command)
            throws CommandDeserializeException {
        if (!command.containsKey("person")) {
            throw new CommandDeserializeException("Unable to parse person");
        }
        try {
            return (Person) command.get("person");
        } catch (ClassCastException exc) {
            throw new CommandDeserializeException("Unable to parse person");
        }
    }

    private static String parsePassportPrefix(Map<String, Object> command)
            throws CommandDeserializeException {
        if (!command.containsKey("passport_prefix")) {
            throw new CommandDeserializeException("Unable to parse passport prefix");
        }
        try {
            return ((String) command.get("passport_prefix"));
        } catch (ClassCastException exc) {
            throw new CommandDeserializeException("Unable to parse passport prefix");
        }
    }

    /**
     * Десериализует объект Map в конкретную команду
     * @param command сериализованный объект
     * @return команда
     * @throws CommandDeserializeException при ошибках десериализации
     */
    public static PersonStorageCommand deserializeCommand(Map<String, Object> command)
            throws CommandDeserializeException {
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
            default -> throw new CommandDeserializeException("Unknown command: " + name);
        };
    }
}
