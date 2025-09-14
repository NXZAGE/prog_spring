package com.itmo.nxzage.server;

import java.util.logging.Logger;
import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

/**
 * Исполнитель команд
 */
public class CommandHandler {
    private static final Logger logger = ServerLogger.getLogger("CommandHandler");
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
            command.checkAccess(services);
            return command.execute(services);
        } catch(IllegalAccessException exc) {
            var response = new ExecutionResponse();
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
            response.setMessage("Permission denied");
            return response;
        } catch (RuntimeException exc) {
            logger.warning("Failed to execute command %s".formatted(command.getClass().toString()));
            exc.printStackTrace();
            var response = new ExecutionResponse();
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
            response.setMessage("Command execution failed: " + exc.getMessage());
            return response;
        }
    }
}
