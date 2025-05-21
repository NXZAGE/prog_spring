package com.itmo.nxzage.server;

import java.util.Map;
import com.itmo.nxzage.common.util.ExecutionResponse;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.serialization.PersonConverter;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/** 
 * Основное приложение сервера
 */
public class App {
    private Storage<Person> storage;
    private PersonStorageServices services;
    private Controller controller;
    private boolean running = false;

    /**
     * Останавливает работу
     */
    @SuppressWarnings("unused")
    private void stop() {
        running = false;
    }

    /**
     * Инициализация
     * @param filename имя файла, в котором хранится коллекция
     */
    public void init(String filename) {
        // TODO сделать что то типо универсальной ошибки пизданувшегося сервера
        storage = new Storage<Person>(filename, new PersonConverter());
        services = new PersonStorageServices(storage);
        controller = new Controller(new CommandHandler(services));
        if (!storage.load()) {
            throw new IllegalStateException("Unable to create storage: wrong filename");
        }
        Person.updateNextID(storage.getAll(running));
    }

    /**
     * Запуск приложения (вечный цикл обработки запросов)
     */
    public void run() {
        running = true;
        while (running);
        // TODO вечный цикл
    }

    /**
     * Обрабатывает одиночный запрос
     * @param request запрос
     * @return результат исполнения запроса
     */
    public ExecutionResponse processRequest(Map<String, Object> request) {
        return controller.processRequest(request);
    }
}
