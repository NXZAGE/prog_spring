package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Обновляет элемент с заданным id в соответсвии с переданным элементом
 */
public final class UpdateCommand extends PersonStorageCommand {
    private Integer id;
    private Person element;

    public UpdateCommand(Integer id, Person element) {
        this.id = id;
        this.element = element;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        if (receiver.baseService().update(id, element)) {
            response.setStatus(OK_STATUS);
            response.setMessage("Successfully updated");
        } else {
            response.setStatus(ERROR_STATUS);
            response.setMessage("Element wasn\'t updated probably because of id is incorrect");
        }
        return response;
    }

}
