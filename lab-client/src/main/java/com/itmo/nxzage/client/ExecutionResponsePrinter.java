package com.itmo.nxzage.client;

import java.util.Collection;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.common.util.ExecutionResponse;

/**
 * Выводит результаты исполнения запросов
 */
public class ExecutionResponsePrinter {
    private OutputHandler out;

    public ExecutionResponsePrinter(OutputHandler out) {
        this.out = out;
    }

    @SuppressWarnings("unchecked")
    private void handleSuccessful(ExecutionResponse response) {
        out.printMessage(response.getMessage() + "\n");
        if (response.contains("person")) {
            out.printMessage(response.getDataElement("person").toString() + "\n");
        }
        if (response.contains("info")) {
            out.printMessage(response.getDataElement("info").toString() + "\n");
        }
        if (response.contains("person_collection")) {
            try {
                out.printCollection((Collection<Object>) response.getDataElement("person_collection"));
            } catch (ClassCastException exc) {
                out.printError("Unable to parse person_collection");
            }
        }
        if (response.contains("nationalities_collection")) {
            try {
                out.printCollection((Collection<Object>) response.getDataElement("nationalities_collection"));
            } catch (ClassCastException exc) {
                out.printError("Unable to parse nationalities_collection");
            }
        }
    }

    private void handleError(ExecutionResponse response) {
        out.printError("[SERVER ERROR] " + response.getMessage() + "\n");
    }

    private void handleCritical(ExecutionResponse response) {
        out.printError("[CRITICAL] " + response.getMessage() + "\n");
    }

    public void handle(ExecutionResponse response) {
        switch (response.getStatus()) {
            case "success" -> handleSuccessful(response);     
            case "error" -> handleError(response);
            case "critical" -> handleCritical(response);   
            default -> out.printError("Unspecified reaponse...\n");
        }
    }
}
