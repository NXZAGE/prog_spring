package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.responses.StringResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Возвращает общую информацию о хранилище
 */
public final class InfoCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new StringResponse();
        String info = receiver.info();
        response.setStatus(OK_STATUS);
        response.setMessage("Storage info successfully loaded");
        response.setData(info);
        return response;
    }

}
