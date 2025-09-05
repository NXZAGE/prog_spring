package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.responses.PersonResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Возвращает элемент по ID
 */
public final class GetElementCommand extends PersonStorageCommand {
    private Integer id;

    public GetElementCommand(Integer id) {
        this.id = id;
    }

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        Person element = receiver.get(id);
        if (element == null) {
            var response = new ExecutionResponse();
            response.setStatus(ERROR_STATUS);
            response.setMessage(String.format("Unable to get element with id {%s}", id.toString()));
            return response;   
        } else {
            var response = new PersonResponse();
            response.setStatus(OK_STATUS);
            response.setMessage("Element successfully got");
            response.setData(element);
            return response;
        }
    }

}
