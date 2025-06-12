package com.itmo.nxzage.client;

import java.util.Collection;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.data.Person;

/**
 * Выводит результаты исполнения запросов
 */
public class ExecutionResponsePrinter {
    private OutputHandler out;

    public ExecutionResponsePrinter(OutputHandler out) {
        this.out = out;
    }

    @SuppressWarnings("unchecked")
    // TODO сделать чек красивее
    private void handleSuccessful(DataContainer response) {
        out.printMessage(response.get("message", String.class) + "\n");
        if (response.has("person")) {
            out.printMessage(response.get("person", Person.class).toString() + "\n");
        }
        if (response.has("info")) {
            
            out.printMessage(response.get("info", String.class).toString() + "\n");
        }
        if (response.has("person_collection")) {
            out.printCollection(response.get("person_collection", Collection.class));
        }
        if (response.has("nationalities_collection")) {
            out.printCollection(response.get("nationalities_collection", Collection.class));
        }
    }

    private void handleError(DataContainer response) {
        out.printError("[SERVER ERROR] " + response.get("message", String.class) + "\n");
    }

    private void handleCritical(DataContainer response) {
        out.printError("[CRITICAL] " + response.get("message", String.class) + "\n");
    }

    public void handle(DataContainer response) {
        response.assertType("status", String.class);
        response.assertType("message", String.class);
        switch (response.get("status", String.class)) {
            case "success" -> handleSuccessful(response);     
            case "error" -> handleError(response);
            case "critical" -> handleCritical(response);   
            default -> out.printError("Unspecified reaponse...\n");
        }
    }
}
