package com.itmo.nxzage.server.exceptions;

/**
 * Класс для стандартных ошибок сервера, которые можно отослать в ExecutionResponse
 */
public class ServerException extends RuntimeException {
    public ServerException() {
        super("Unable to process required action");
    }

    public ServerException(String message) {
        super(message);
    }

    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
