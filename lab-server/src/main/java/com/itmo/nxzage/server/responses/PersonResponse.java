package com.itmo.nxzage.server.responses;

import com.itmo.nxzage.common.util.data.Person;

public final class PersonResponse extends ExecutionResponse {
    Person data; 

    public Person getData() {
        return data;
    }

    public void setData(Person data) {
        this.data = data;
    }
}
