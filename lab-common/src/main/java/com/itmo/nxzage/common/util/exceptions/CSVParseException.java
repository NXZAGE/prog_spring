package com.itmo.nxzage.common.util.exceptions;

/**
 * Исключение, потенциально вызываемое при невозможности десериализовать CSV строку
 */
public class CSVParseException extends Exception{
    public CSVParseException() {
        super("Unable to parse CSV string");   
    }

    public CSVParseException(String message) {
        super(message);
    }

    public CSVParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
