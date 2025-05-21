package com.itmo.nxzage.server.commands;

import java.util.Collection;
import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Команда для получения полной коллекции Person
 */
public final class GetAllCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        Collection<Person> collection = receiver.baseService().getCollection();
        response.setStatus(PersonStorageCommand.OK_STATUS);
        response.setMessage("Collection successfully got");
        response.applyData("person_collection", collection);

        return response;
    }

}
