package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Сохраняет коллекцию в файл
 */
public final class SaveCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new ExecutionResponse();
        receiver.dump();
        response.setStatus(OK_STATUS);
        response.setMessage("Collection saved to file");

        return response;
    }

}
