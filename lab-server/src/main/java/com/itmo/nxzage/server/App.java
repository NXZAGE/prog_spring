package com.itmo.nxzage.server;

import java.net.SocketException;
import java.util.Map;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.serialization.PersonConverter;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.net.InteractionContext;
import com.itmo.nxzage.server.services.net.UDPTransportService;
import com.itmo.nxzage.server.services.storage.PersonStorageServices;

/**
 * Основное приложение сервера
 */
public final class App {
    private static final int PORT = 3777; 
    private static final int BUFFER_SIZE = 8192;
    private Storage<Person> storage;
    private PersonStorageServices services;
    private UDPTransportService transportService;
    private Controller controller;
    private boolean running = false;
    private Logger logger = ServerLogger.getLogger("App");

    /**
     * Останавливает работу
     */
    @SuppressWarnings("unused")
    private void stop() {
        logger.info("Server stoped");
        running = false;
    }

    /**
     * Инициализация
     * 
     * @param filename имя файла, в котором хранится коллекция
     */
    public void init(String filename) {
        // TODO сделать что то типо универсальной ошибки пизданувшегося сервера
        storage = new Storage<Person>(filename, new PersonConverter());
        logger.info("Storage initialized");
        services = new PersonStorageServices(storage);
        logger.info("Storage Services initialized");
        controller = new Controller(new CommandHandler(services));
        logger.info("Controller initialized");
        if (!storage.load()) {
            logger.severe("Storage connection to file failed");
            throw new IllegalStateException("Unable to create storage: wrong filename");
        }
        Person.updateNextID(storage.getAll(running));
        logger.info("Peron NextID updated");
        try {
            transportService = new UDPTransportService(PORT, BUFFER_SIZE);
            logger.info("UDP Transport Service initialized");
        } catch (SocketException e) {
            logger.severe("UDP Transport Service initialization failed: " + e.getMessage());
            throw new IllegalStateException("Network connection failed: wrong port");
        }
    }

    /**
     * Запуск приложения (вечный цикл обработки запросов)
     */
    public void run() {
        logger.info("Server started");
        running = true;
        while (running) {
            try {
                InteractionContext interaction = transportService.receiveRequest();  
                controller.handle(interaction);
                transportService.sendResponse(interaction);
            } catch (RuntimeException e) {
                logger.warning("Failed during processing request. Details: " + e.getMessage());
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * Обрабатывает одиночный запрос
     * 
     * @param request запрос
     * @return результат исполнения запроса
     */
    @Deprecated
    public ExecutionResponse processRequest(Map<String, Object> request) {
        return controller.processRequest(request);
    }
}
