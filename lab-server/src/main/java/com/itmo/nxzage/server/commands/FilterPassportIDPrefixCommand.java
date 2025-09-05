package com.itmo.nxzage.server.commands;

import java.util.Collection;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.responses.PersonCollectionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Возвращает элементы, passportID которых начинается с заданного префикса
 */
public final class FilterPassportIDPrefixCommand extends PersonStorageCommand {
    private String prefix;

    public FilterPassportIDPrefixCommand(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new PersonCollectionResponse();
        Collection<Person> collection = receiver.filterPassportIDPrefix(prefix);
        if (collection.isEmpty()) {
            response.setStatus(ERROR_STATUS);
            response.setMessage(
                    String.format("There is no element with passportID starts with {%s}", prefix));
        } else {
            response.setStatus(OK_STATUS);
            response.setMessage("Successfully loaded");
            response.setData(collection.stream().toList());
        }
        return response;
    }

}
