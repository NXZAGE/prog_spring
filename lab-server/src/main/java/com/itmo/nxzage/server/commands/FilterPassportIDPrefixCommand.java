package com.itmo.nxzage.server.commands;

import java.util.Collection;
import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Возвращает элементы, passportID которых начинается с заданного префикса
 */
public final class FilterPassportIDPrefixCommand extends PersonStorageCommand {
    private String prefix;

    public FilterPassportIDPrefixCommand(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        Collection<Person> collection = receiver.personService().filterPassportIDPrefix(prefix);
        if (collection.isEmpty()) {
            response.setStatus(ERROR_STATUS);
            response.setMessage(
                    String.format("There is no element with passportID starts with {%s}", prefix));
        } else {
            response.setStatus(OK_STATUS);
            response.setMessage("Successfully loaded");
            response.applyData("person_collection", collection);
        }
        return response;
    }

}
