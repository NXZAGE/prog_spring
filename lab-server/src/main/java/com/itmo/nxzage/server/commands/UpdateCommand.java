package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

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
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new ExecutionResponse();
        if (receiver.update(id, element)) {
            response.setStatus(OK_STATUS);
            response.setMessage("Successfully updated");
        } else {
            response.setStatus(ERROR_STATUS);
            response.setMessage("Element wasn\'t updated probably because of id is incorrect");
        }
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

    public void checkAccess(PersonStorageService e) throws IllegalAccessException {
        Person original = e.get(id);
        if (original == null) {
            return; 
        }
        if (!original.getOwner().equals(this.user)) {
            throw new IllegalAccessException("Permission denied");
        }
    }
}
