package com.itmo.nxzage.server.commands;

import java.util.Collection;
import com.itmo.nxzage.common.util.data.Country;
import com.itmo.nxzage.server.responses.CountryCollectionResponse;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Возвращает поля nationality в порядке возрастания
 */
public final class GetNationalitiesAscCommand extends PersonStorageCommand {
    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new CountryCollectionResponse();
        Collection<Country> collection = receiver.getNationalityAscending();
        response.setStatus(OK_STATUS);
        response.setMessage("Successfully loaded nationality fields");
        response.setData(collection.stream().toList());
        return response;
    }

}
