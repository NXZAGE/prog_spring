package com.itmo.nxzage.server.commands;

import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Абстрактный класс команды, которая рабоатет с хранилищем Person
 */
public abstract class PersonStorageCommand implements Command<ExecutionResponse, PersonStorageServices> {
    // константы для заполнения полдей ExecutionResponce.status
    public static final String OK_STATUS = "success";
    public static final String ERROR_STATUS = "error";
    public static final String CRITICAL_STATUS = "critical"; 
}
