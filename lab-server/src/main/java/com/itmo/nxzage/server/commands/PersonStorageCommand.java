package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.data.User;
import com.itmo.nxzage.server.responses.ExecutionResponse;
import com.itmo.nxzage.server.services.auth.Accessable;
import com.itmo.nxzage.server.services.storage.PersonStorageService;

// TODO toString
/**
 * Абстрактный класс команды, которая рабоатет с хранилищем Person
 */
public abstract class PersonStorageCommand implements Command<ExecutionResponse, PersonStorageService>, Accessable<PersonStorageService> {
    // константы для заполнения полдей ExecutionResponce.status
    public static final String OK_STATUS = "success";
    public static final String ERROR_STATUS = "error";
    public static final String CRITICAL_STATUS = "critical"; 
    protected User user;

    public void setUser(User user) {
        if (this.user != null) {
            throw new IllegalStateException("User already set");
        }
        this.user = user;
    }
}
