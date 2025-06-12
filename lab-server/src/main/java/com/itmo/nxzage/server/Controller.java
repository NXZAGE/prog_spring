package com.itmo.nxzage.server;

import java.util.Map;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.commands.SaveCommand;
import com.itmo.nxzage.server.exceptions.CommandDeserializeException;
import com.itmo.nxzage.server.exceptions.CommandRecognitionException;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.net.InteractionContext;
import com.itmo.nxzage.server.services.net.CacheService;

/**
 * Контроллер, который обрабатывает запрос и дилигирует исполнение
 */
public class Controller {
    private CommandHandler executor;
    private CommandRecognizer recognizer;
    private CacheService cache;
    private final Logger logger = ServerLogger.getLogger("Controller");

    public Controller(CommandHandler handler) {
        executor = handler;
        recognizer = new CommandRecognizer();
        cache = new CacheService();
    }

    public void handle(InteractionContext interaction) {
        switch (interaction.getRequest().getType()) {
            case PING -> handlePing(interaction);
            case REQUEST -> handleRequest(interaction);
            default -> throw new IllegalArgumentException("Unsupported interaction type");
        };
    }

    private void handlePing(InteractionContext interaction) {
        // TODO реалзовать (ping)
    }

    private void handleRequest(InteractionContext interaction) {
        if (!interaction.getRequest().getType().equals(PacketType.REQUEST)) {
            throw new IllegalArgumentException("Interaction is not a request");
        }
        if (cache.hit(interaction)) {
            return;
        }
        ExecutionResponse response;
        try {
            var command = recognizer.recognize(interaction.getRequest().getPayload());
            logger.info("Command successfully recognized");
            response = executor.execute(command);
            logger.info("Command successfully executed");
        } catch (CommandRecognitionException e) {
            // TODO log ?
            logger.info("Failed to recognize command from request");
            response = new ExecutionResponse();
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
            response.setMessage("Unrecognizable request content. " + e.getMessage());
        }
        compliteInteraction(interaction, response);
        logger.info("Packed responses added to Interaction Context");
        cache.memorize(interaction);
        executor.execute(new SaveCommand());
    }

    private void compliteInteraction(InteractionContext interaction, ExecutionResponse response) {
        ExecutionResponsePacker.pack(response).forEach(packet -> interaction.addResponse(packet));
    }

    /**
     * Исполняет запрос
     * 
     * @param request запрос
     * @return результат исполнения
     */
    @Deprecated
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
