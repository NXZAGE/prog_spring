package com.itmo.nxzage.server.services.storage;

import java.util.Collection;
import com.itmo.nxzage.common.util.data.DataElement;
import com.itmo.nxzage.server.Storage;

/**
 * Универсальный сервис Storage с базовым интерфейсом (CRUD операции)
 */
public class BaseStorageService<T extends DataElement<T>> {
    Storage<T> storage;

    public BaseStorageService(Storage<T> storage) {
        this.storage = storage;
    }

    /**
     * Получает элемент по id
     * @param id 
     * @return элемент с заданным id
     */
    public T get(Integer id) {
        return storage.get(id);
    }

    /**
     * Возвращает полную отсортированную по возврастанию коллекцию
     * @return отсортированная по возрастанию коллекция
     */
    public Collection<T> getCollection() {
        return storage.getAll(true);
    }

    /**
     * Возвращает полную отсортированную по убыванию коллекцию
     * @return отсортированная по убыванию коллекция
     */
    public Collection<T> getReversedCollection() {
        return storage.getAll(false);
    }

    /**
     * Обновляет элемент с заданным id в соответствии с переданным элементом 
     * @param id id обновляемого элемента
     * @param value эталон
     * @return true, если произошло обновление
     */
    public boolean update(Integer id, T value) {
        return storage.update(id, value);
    }

    /**
     * Добавляет заданный элемент в коллекцию
     * @param element новый элемент
     */
    public void add(T element) {
        storage.add(element);
    }

    /**
     * Удаляет элемент с заданным id
     * @param id
     * @return true, если элемент был удален
     */
    public boolean remove(Integer id) {
        return storage.remove(id);
    }


    /**
     * Возвращает общую информацию о коллекции
     * 
     * @return общую информацию о коллекции
     */
    public String info() {
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
    public void clear() {
        storage.clear();
    }
}
