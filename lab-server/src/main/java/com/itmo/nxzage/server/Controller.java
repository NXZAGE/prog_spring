package com.itmo.nxzage.server;

import java.util.Map;
import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.exceptions.CommandDeserializeException;

/**
 * Контроллер, который обрабатывает запрос и дилигирует исполнение
 */
public class Controller {
    CommandHandler executor;

    public Controller(CommandHandler handler) {
        executor = handler;
    }

    /**
     * Исполняет запрос
     * @param request запрос
     * @return результат исполнения
     */
    ExecutionResponse processRequest(Map<String, Object> request) {
        ExecutionResponse response;
        try {
            PersonStorageCommand command = CommandDeserializer.deserializeCommand(request);
            response = executor.execute(command);
        } catch (CommandDeserializeException exc) {
            response = new ExecutionResponse();
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
            response.setMessage("Incorerct request format. " + exc.getMessage());
        }
        return response;
    }
}
