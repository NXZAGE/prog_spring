package com.itmo.nxzage.server.commands;

import java.util.Collection;
import com.itmo.nxzage.common.util.data.Country;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Возвращает поля nationality в порядке возрастания
 */
public final class GetNationalitiesAscCommand extends PersonStorageCommand {
    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        Collection<Country> collection = receiver.personService().getNationalityAscending();
        response.setStatus(OK_STATUS);
        response.setMessage("Successfully loaded nationality fields");
        response.put("nationalities_collection", collection);
        response.setHeavy();
        response.setHeavyKey("nationalities_collection");
        response.setHeavyType(Country.class);
        return response;
    }

}
