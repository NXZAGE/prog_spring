package com.itmo.nxzage.server.exceptions;

/**
 * Критическая ошибка сервера
 */
public class ServerError extends Error {
    public ServerError() {
        super("Critical server error");
    }

    public ServerError(String message) {
        super(message);
    }
    
    public ServerError(String message, Throwable cause) {
        super(message, cause);
    }
}
