package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Очищает коллекцию
 */
public final class ClearCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        receiver.baseService().clear();
        response.setStatus(OK_STATUS);
        response.setMessage("Collection cleared");
        return response;
    }

}
