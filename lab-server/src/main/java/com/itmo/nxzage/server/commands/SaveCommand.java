package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.server.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Сохраняет коллекцию в файл
 */
public final class SaveCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        receiver.baseService().dump();
        response.setStatus(OK_STATUS);
        response.setMessage("Collection saved to file");

        return response;
    }

}
