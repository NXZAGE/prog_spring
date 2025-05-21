package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Добавляет новый элемент в коллекцию, если он станет максимальным
 */
public final class AddIfMaxCommand extends PersonStorageCommand {
    private Person element;

    public AddIfMaxCommand(Person element) {
        this.element = element;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        if (receiver.personService().addIfMax(element)) {
            response.setMessage("Element successfully added");
        } else {
            response.setMessage("Element wasn\'t added \'cause it isn\'t max");
        }
        response.setStatus(OK_STATUS);
        return response;
    }

}
