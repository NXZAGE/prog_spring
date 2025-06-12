package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Удаляет из коллекции элементы ментьше, чем заданный
 * <p>
 * Возвращает количество удаленных элементов
 * </p>
 */
public final class RemoveLowerCommand extends PersonStorageCommand {
    private Person element;

    public RemoveLowerCommand(Person element) {
        this.element = element;
    }

    @Override
    public ExecutionResponse execute(PersonStorageServices receiver) {
        var response = new ExecutionResponse();
        Integer count = receiver.personService().removeLower(element);
        response.setStatus(OK_STATUS);
        response.setMessage(String.format("Removed %d elements", count));
        response.put("removed_count", count);
        return response;
    }

}
