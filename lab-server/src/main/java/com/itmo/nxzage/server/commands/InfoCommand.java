package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Возвращает общую информацию о хранилище
 */
public final class InfoCommand extends PersonStorageCommand {

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        String info = receiver.baseService().info();
        response.setStatus(OK_STATUS);
        response.setMessage("Storage info successfully loaded");
        response.applyData("info", info);
        return response;
    }

}
