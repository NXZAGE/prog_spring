package com.itmo.nxzage.client;

import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.client.net.NetworkManager;
import com.itmo.nxzage.common.util.data.DataContainer;

/**
 * Класс, который отправляет команды на сервер
 */
public class CommandExecutor {
    private NetworkManager networkManager;

    public CommandExecutor(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    /**
     * Передает команду серверу на исполнения
     * 
     * @param command исполняемая команда
     * @return ответ сервера
     */
    public DataContainer execute(Command command) {
        return networkManager.interact(command);
    }
}
