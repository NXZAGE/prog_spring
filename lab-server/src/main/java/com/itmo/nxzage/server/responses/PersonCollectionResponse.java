package com.itmo.nxzage.server.responses;

import java.util.List;
import com.itmo.nxzage.common.util.data.Person;

public final class PersonCollectionResponse extends ExecutionResponse {
    List<Person> data; 

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }
}
