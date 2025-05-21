package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Добавляет новый элемент в коллекцию
 */
public final class AddCommand extends PersonStorageCommand {
    private Person element;

    // TODO id блять добавить ОЧЕНЬ НАДО Я СУКА ЗАБУДУ ПРО ЭТО
    public AddCommand(Person element) {
        this.element = element;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        receiver.baseService().add(element);
        response.setStatus(OK_STATUS);
        response.setMessage("Element successfully added");
        return response;
    }

}
