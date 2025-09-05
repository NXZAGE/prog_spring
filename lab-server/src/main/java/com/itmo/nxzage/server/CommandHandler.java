package com.itmo.nxzage.server;

import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Исполнитель команд
 */
public class CommandHandler {
    private PersonStorageService services;

    public CommandHandler(PersonStorageService services) {
        this.services = services;
    }

    /**
     * Исполняет команду
     * @param command команда
     * @return результат исполнения
     */
    public ExecutionResponse execute(PersonStorageCommand command) {
        try {
            return command.execute(services);
        } catch (RuntimeException exc) {
            var response = new ExecutionResponse();
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
            response.setMessage("Command execution failed: " + exc.getMessage());
            return response;
        }
    }
}
