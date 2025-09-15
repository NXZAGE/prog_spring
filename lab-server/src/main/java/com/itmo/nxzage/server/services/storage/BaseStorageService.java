package com.itmo.nxzage.server.services.storage;

import java.util.Collection;
import com.itmo.nxzage.common.util.data.DataElement;
import com.itmo.nxzage.server.Storage;
import com.itmo.nxzage.server.services.db.dao.Dao;

/**
 * Универсальный сервис Storage с базовым интерфейсом (CRUD операции)
 */
public class BaseStorageService<T extends DataElement<T>> {
    protected Storage<T> storage;

    public BaseStorageService(Storage<T> storage) {
        this.storage = storage;
        // TODO синхронизация с дао
    }

    /**
     * Получает элемент по id
     * @param id 
     * @return элемент с заданным id
     */
    public synchronized T get(Integer id) {
        return storage.get(id);
    }

    /**
     * Возвращает полную отсортированную по возврастанию коллекцию
     * @return отсортированная по возрастанию коллекция
     */
    public synchronized Collection<T> getCollection() {
        return storage.getAll(true).toList();
    }

    /**
     * Возвращает полную отсортированную по убыванию коллекцию
     * @return отсортированная по убыванию коллекция
     */
    public synchronized Collection<T> getReversedCollection() {
        return storage.getAll(false).toList();
    }

    /**
     * Обновляет элемент с заданным id в соответствии с переданным элементом 
     * @param id id обновляемого элемента
     * @param value эталон
     * @return true, если произошло обновление
     */
    public synchronized boolean update(Integer id, T value) {
        return storage.update(id, value);
    }

    /**
     * Добавляет заданный элемент в коллекцию
     * @param element новый элемент
     */
    public synchronized void add(T element) {
        storage.add(element);
    }

    /**
     * Удаляет элемент с заданным id
     * @param id
     * @return true, если элемент был удален
     */
    public synchronized boolean remove(Integer id) {
        return storage.remove(id);
    }


    /**
     * Возвращает общую информацию о коллекции
     * 
     * @return общую информацию о коллекции
     */
    public synchronized String info() {
        return storage.info();
    }

    /**
     * Сохраняет коллекцию в файл
     */
    public void dump() {
        storage.dump();
    }

    /**
     * Очищает коллекцию
     */
    public synchronized void clear() {
        storage.clear();
    }
}
