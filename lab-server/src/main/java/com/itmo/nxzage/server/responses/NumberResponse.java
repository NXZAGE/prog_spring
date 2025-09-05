package com.itmo.nxzage.server.responses;

public final class NumberResponse extends ExecutionResponse {
    Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
