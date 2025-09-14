package com.itmo.nxzage.server.responses;

import java.util.List;
import com.itmo.nxzage.common.util.data.DataContainer;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.net.response.ResponseType;

public final class PersonCollectionResponse extends ExecutionResponse {
    List<Person> data; 

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }

    @Override
    public DataContainer packToDataContainer() {
        return new DataContainer()
            .put("status", status)
            .put("message", message)
            .put("response_type", ResponseType.PERSON_COLLECTION)
            .put("data", data);
    }
}
