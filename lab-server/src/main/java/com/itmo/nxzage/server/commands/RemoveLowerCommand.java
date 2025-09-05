package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.responses.NumberResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

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
    public ExecutionResponse execute(PersonStorageService receiver) {
        var response = new NumberResponse();
        Integer count = receiver.removeLower(element);
        response.setStatus(OK_STATUS);
        response.setMessage(String.format("Removed %d elements", count));
        response.setValue(count);
        return response;
    }

}
