package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

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
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new ExecutionResponse();
        receiver.add(element);
        response.setStatus(OK_STATUS);
        response.setMessage("Element successfully added");
        return response;
    }

    @Override
    public void setUser(User user) {
        if (this.user != null) {
            throw new IllegalStateException("User already set");
        }
        this.user = user;
        this.element.setOwner(user);
    }
}
