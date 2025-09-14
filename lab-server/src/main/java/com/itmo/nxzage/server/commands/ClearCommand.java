package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Очищает коллекцию
 */
public final class ClearCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new ExecutionResponse();
        receiver.clear(this.user.getId());
        response.setStatus(OK_STATUS);
        response.setMessage("Collection cleared");
        return response;
    }
}
