package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Удаляет элемент с заданным id
 */
public final class RemoveByID extends PersonStorageCommand {
    private Integer id;

    public RemoveByID(Integer id) {
        this.id = id;
    }

    @Override
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new ExecutionResponse();
        if (receiver.remove(id)) {
            response.setStatus(OK_STATUS);
            response.setMessage("Successfully removed");
        } else {
            response.setStatus(ERROR_STATUS);
            response.setMessage("Incorrect id. Element wasn\'t deleted");
        }
        return response;
    }

}
