package com.itmo.nxzage.server.commands;

import java.util.Collection;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.responses.PersonCollectionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Команда для получения полной коллекции Person
 */
public final class GetAllCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new PersonCollectionResponse();
        Collection<Person> collection = receiver.getCollection();
        response.setStatus(PersonStorageCommand.OK_STATUS);
        response.setMessage("Collection successfully got");
        response.setData(collection.stream().toList());

        return response;
    }

}
