package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Добавляет новый элемент в коллекцию, если он станет минимальнысм
 */
public final class AddIfMinCommand extends PersonStorageCommand {
    private Person element;

    public AddIfMinCommand(Person element) {
        this.element = element;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        if (receiver.personService().addIfMin(element)) {
            response.setMessage("Element successfully added");
        } else {
            response.setMessage("Element wasn\'t added \'cause it isn\'t min");
        }
        response.setStatus(OK_STATUS);
        return response;
    }

}
