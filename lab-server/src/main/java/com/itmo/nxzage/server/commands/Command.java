package com.itmo.nxzage.server.commands;

/**
 * Интерфейс команды
 */
@FunctionalInterface
public interface Command<T, R> {
    public T execute(R receiver); 
}
