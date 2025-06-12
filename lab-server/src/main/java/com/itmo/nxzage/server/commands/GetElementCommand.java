package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Возвращает элемент по ID
 */
public final class GetElementCommand extends PersonStorageCommand {
    private Integer id;

    public GetElementCommand(Integer id) {
        this.id = id;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        Person element = receiver.baseService().get(id);
        if (element == null) {
            response.setStatus(ERROR_STATUS);
            response.setMessage(String.format("Unable to get element with id {%s}", id.toString()));
        } else {
            response.setStatus(OK_STATUS);
            response.setMessage("Element successfully got");
        }
        return response;
    }

}
