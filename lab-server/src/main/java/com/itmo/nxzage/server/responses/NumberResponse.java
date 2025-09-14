package com.itmo.nxzage.server.responses;

import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.net.response.ResponseType;

public final class NumberResponse extends ExecutionResponse {
    Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public DataContainer packToDataContainer() {
        return new DataContainer()
            .put("status", status)
            .put("message", message)
            .put("response_type", ResponseType.NUMBER)
            .put("value", value);
    }
}
