package com.itmo.nxzage.server.responses;

import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.net.response.ResponseType;

public final class StringResponse extends ExecutionResponse {
    String data;

    public String getData() {
        return data;
    }

    public void setData(String value) {
        this.data = value;
    }

    @Override
    public DataContainer packToDataContainer() {
        return new DataContainer()
            .put("status", status)
            .put("message", message)
            .put("response_type", ResponseType.STRINGS)
            .put("data", data);
    }
}
