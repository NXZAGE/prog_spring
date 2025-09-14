package com.itmo.nxzage.server.responses;

import java.io.Serializable;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.net.response.ResponseType;

public sealed class ExecutionResponse implements Serializable permits PersonResponse, NumberResponse, StringResponse, CountryCollectionResponse, PersonCollectionResponse{
    protected String status;
    protected String message;


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

    public DataContainer packToDataContainer() {
        return new DataContainer()
            .put("status", status)
            .put("message", message)
            .put("response_type", ResponseType.DEFAULT);
    }
}
