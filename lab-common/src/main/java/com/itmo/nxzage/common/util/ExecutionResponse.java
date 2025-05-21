package com.itmo.nxzage.common.util;

import java.util.HashMap;
import java.util.Map;


// TODO LAB 6 move to server
public class ExecutionResponse {
    private String status;
    private String message;
    private Map<String, Object> data;

    {
        data = new HashMap<String, Object>();
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

    public Map<String, Object> getData() {
        return data;
    }

    public void applyData(String key, Object value) {
        data.put(key, value);
    }
    
    public boolean contains(String key) {
        return data.containsKey(key);
    }

    public Object getDataElement(String key) {
        return data.get(key);
    }
}
