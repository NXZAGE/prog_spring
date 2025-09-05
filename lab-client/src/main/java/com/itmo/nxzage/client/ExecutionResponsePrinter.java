package com.itmo.nxzage.client;

import java.util.ArrayList;
import java.util.Collection;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.net.response.DTO.CountryCollectionResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.NumberResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.PersonCollectionResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.PersonResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.ResponseDTO;
import com.itmo.nxzage.common.util.net.response.DTO.StringResponseDTO;

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
    private void handleSuccessful(ResponseDTO response) {
        out.printMessage(response.message() + "\n");
        // if (response.has("person")) {
        //     out.printMessage(response.get("person", Person.class).toString() + "\n");
        // }
        // if (response.has("info")) {
            
        //     out.printMessage(response.get("info", String.class).toString() + "\n");
        // }
        // if (response.has("person_collection")) {
        //     out.printCollection(response.get("person_collection", Collection.class));
        // }
        // if (response.has("nationalities_collection")) {
        //     out.printCollection(response.get("nationalities_collection", Collection.class));
        // }
        if (response instanceof PersonResponseDTO r) {
            out.printMessage(r.data().toString() + "\n");
        } else if (response instanceof StringResponseDTO r) {
            out.printMessage(r.data() + "\n");
        } else if (response instanceof NumberResponseDTO r) {
            out.printError(r.value().toString() + "\n");
        } else if (response instanceof PersonCollectionResponseDTO r) {
            out.printCollection(r.data().stream().map(obj -> (Object) obj).toList());
        } else if (response instanceof CountryCollectionResponseDTO r) {
            out.printCollection(r.data().stream().map(obj -> (Object) obj).toList());
        }
    }

    private void handleError(ResponseDTO response) {
        out.printError("[SERVER ERROR] " + response.message() + "\n");
    }

    private void handleCritical(ResponseDTO response) {
        out.printError("[CRITICAL] " + response.message() + "\n");
    }

    public void handle(ResponseDTO response) {
        // TODO realisation
        
        // response.assertType("status", String.class);
        // response.assertType("message", String.class);
        switch (response.status()) {
            case "success" -> handleSuccessful(response);     
            case "error" -> handleError(response);
            case "critical" -> handleCritical(response);   
            default -> out.printError("Unspecified reaponse...\n");
        }
    }
}
