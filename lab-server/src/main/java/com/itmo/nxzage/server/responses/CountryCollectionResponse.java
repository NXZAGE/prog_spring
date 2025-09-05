package com.itmo.nxzage.server.responses;

import java.util.List;
import com.itmo.nxzage.common.util.data.Country;

public final class CountryCollectionResponse extends ExecutionResponse {
    List<Country> data;

    public List<Country> getData() {
        return data;
    }

    public void setData(List<Country> data) {
        this.data = data;
    }
}
