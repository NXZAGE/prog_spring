package com.itmo.nxzage.server;

import com.itmo.nxzage.common.util.data.DataContainer;


public class ExecutionResponse {
    private String status;
    private String message;
    private boolean heavy;
    private DataContainer data;

    {
        heavy = false;
        data = new DataContainer();
    }

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

    public boolean isHeavy() {
        return heavy;
    }

    public void setHeavy() {
        heavy = true;
    }

    public void setHeavyKey(String key) {
        put("heavy_key", key);
    }

    public <T> void setHeavyType(Class<T> type) {
        put("heavy_type", type);
    }

    public DataContainer getData() {
        return data;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }
}
