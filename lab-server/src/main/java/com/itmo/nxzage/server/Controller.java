package com.itmo.nxzage.server;

import java.util.Map;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.common.util.net.Packet;
import com.itmo.nxzage.common.util.net.PacketType;
import com.itmo.nxzage.server.commands.PersonStorageCommand;
import com.itmo.nxzage.server.exceptions.AuthenticationException;
import com.itmo.nxzage.server.exceptions.CommandDeserializeException;
import com.itmo.nxzage.server.exceptions.CommandRecognitionException;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.net.InteractionContext;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.auth.AuthenticationService;
import com.itmo.nxzage.server.services.net.CacheService;

/**
 * Контроллер, который обрабатывает запрос и дилигирует исполнение
 */
public class Controller {
    private CommandHandler executor;
    private CommandRecognizer recognizer;
    private CacheService cache;
    private AuthenticationService auth;
    private final Logger logger = ServerLogger.getLogger("Controller");

    public Controller(CommandHandler handler) {
        executor = handler;
        recognizer = new CommandRecognizer();
        cache = new CacheService();
        auth = new AuthenticationService();
    }

    public void handle(InteractionContext interaction) {
        switch (interaction.getRequest().getType()) {
            case PING -> handlePing(interaction);
            case REQUEST -> handleRequest(interaction);
            case REGISTER_REQUEST -> handleRegisterRequest(interaction);
            default -> throw new IllegalArgumentException("Unsupported interaction type");
        };
    }

    private void handlePing(InteractionContext interaction) {
        // TODO реалзовать (ping)
        if (!interaction.getRequest().getType().equals(PacketType.PING)) {
            throw new IllegalArgumentException("Interaction is not a ping");
        }
        var response = new ExecutionResponse();
        try {
            auth.auth(interaction.getRequest());
            response.setMessage("Your login approved!");
            response.setStatus(PersonStorageCommand.OK_STATUS);
        } catch (AuthenticationException e) {
            response.setMessage("Authemtication failed: " + e.getMessage());
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
        }
        compliteInteraction(interaction, response);
    }

    private void handleRegisterRequest(InteractionContext interaction) {
        // TODO realise
        if (!interaction.getRequest().getType().equals(PacketType.REGISTER_REQUEST)) {
            throw new IllegalArgumentException("Interaction is not a register request");
        }
        ExecutionResponse response = new ExecutionResponse();
        try {
            auth.registerNewUser(interaction.getRequest());
            response.setMessage("New user successfully created!");
            response.setStatus(PersonStorageCommand.OK_STATUS);
        } catch (AuthenticationException e) {
            response.setMessage(e.getMessage());
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
        } catch (RuntimeException e) {
            response.setMessage(e.getMessage());
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
        }
        compliteInteraction(interaction, response);
    }

    private void handleRequest(InteractionContext interaction) {
        // TODO передавать сюда packet и возвращать response(executionresponse)
        if (!interaction.getRequest().getType().equals(PacketType.REQUEST)) {
            throw new IllegalArgumentException("Interaction is not a request");
        }
        if (cache.hit(interaction)) {
            return;
        }
        ExecutionResponse response;
        try {
            User user = auth.auth(interaction.getRequest());
            var command = recognizer.recognize(interaction.getRequest().getPayload());
            logger.info("Command successfully recognized");
            command.setUser(user);
            response = executor.execute(command);
            logger.info("Command successfully executed");
        } catch (AuthenticationException e) {
            response = new ExecutionResponse();
            response.setMessage("Authentication failed: " + e.getMessage());
            response.setStatus(PersonStorageCommand.ERROR_STATUS);
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
    }

    private void compliteInteraction(InteractionContext interaction, ExecutionResponse response) {
        Packet responsePacket = new Packet(PacketType.RESPONSE, response.packToDataContainer());
        responsePacket.setInteractionID(interaction.getID()); // TODO убрать, если убрал interactionID
        interaction.setResponse(responsePacket);
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
