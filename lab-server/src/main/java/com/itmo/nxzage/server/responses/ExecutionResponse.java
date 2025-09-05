package com.itmo.nxzage.server.responses;

import java.io.Serializable;

public sealed class ExecutionResponse implements Serializable permits PersonResponse, NumberResponse, StringResponse, CountryCollectionResponse, PersonCollectionResponse{
    private String status;
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
