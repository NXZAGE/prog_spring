package com.itmo.nxzage.server.responses;

public final class StringResponse extends ExecutionResponse {
    String data;

    public String getData() {
        return data;
    }

    public void setData(String value) {
        this.data = value;
    }
}
