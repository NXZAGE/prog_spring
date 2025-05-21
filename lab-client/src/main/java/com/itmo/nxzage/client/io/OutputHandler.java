package com.itmo.nxzage.client.io;

import java.util.Collection;

/**
 * Интерфейс для унификации вывода
 */
public interface OutputHandler {
    public void printMessage(String message);
    /**
     * Вывести подсказку для ввода поля
     * <p> <i> Интерактивный режим </i> </p>
     * @param prompt подсказка
     */
    public void printPrompt(String prompt);
    public void printError(String errorMessage);
    public void printCollection(Collection<Object> collection);
    public void printSpecial(String message);
    public void enablePrompts();
    public void disablePrompts();
    public void mute();
    public void unmute();
}
