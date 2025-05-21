package com.itmo.nxzage.client;

import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.server.App;

/**
 * Класс, который отправляет команды на сервер
 */
public class CommandExecutor {
    private App server;

    public CommandExecutor(App server) {
        this.server = server;
    }

    /**
     * Передает команду серверу на исполнения
     * 
     * @param command исполняемая команда
     * @return ответ сервера
     */
    public ExecutionResponse execute(Command command) {
        return server.processRequest(command.toMap());
    }
}
