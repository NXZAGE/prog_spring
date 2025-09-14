package com.itmo.nxzage.server.responses;

import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.net.response.ResponseType;

public final class PersonResponse extends ExecutionResponse {
    Person data; 

    public Person getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }

    @Override
    public DataContainer packToDataContainer() {
        return new DataContainer()
            .put("status", status)
            .put("message", message)
            .put("response_type", ResponseType.PERSON)
            .put("data", data);
    }
}
