package com.itmo.nxzage.server.services.storage;

import java.util.Collection;
import java.util.List;
import com.itmo.nxzage.common.util.data.Country;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.Storage;

/**
 * Сервис, с расширенным интерфейсом для Storage из объектов типа Person
 */
public class PersonStorageService {
    Storage<Person> storage;

    public PersonStorageService(Storage<Person> storage) {
        this.storage = storage;
    }

    /**
     * Добавляет в коллекцию элемент, если он меньше текущего минимального
     * 
     * @param element новый элемент
     * @return true, если произошло добавление
     */
    public boolean addIfMin(Person element) {
        if (storage.isEmpty() || element.compareTo(storage.min()) < 0) {
            storage.add(element);
            return true;
        }
        return false;
    }

    /**
     * Добавляет в коллекцию элемент, если он больше текущего максимального
     * 
     * @param element новый элемент
     * @return true, если произошло добавление
     */
    public boolean addIfMax(Person element) {
        if (storage.isEmpty() || element.compareTo(storage.max()) > 0) {
            storage.add(element);
            return true;
        }
        return false;
    }

    /**
     * Удаляет элементы меньше заданного
     * @param reference заданный элемент
     * @return количество удаленных элементов
     */
    public int removeLower(Person reference) {
        List<Person> toRemove = storage.getAll(false).stream()
                .takeWhile((element) -> (element.compareTo(reference) < 0)).toList();
        toRemove.forEach((element) -> storage.remove(element.getID()));
        return toRemove.size();
    }

    /**
     * Возвращает коллекцию со значениями nationality отсортированной по возрастанию коллекции
     * @return коллекция со значениями nationality
     */
    public Collection<Country> getNationalityAscending() {
        return storage.getAll(false).stream().map((element) -> element.getNationality()).toList();
    }

    /**
     * Возвращает коллекцию со значением nationality отсортированной по убыванию коллекции 
     * @return коллекция со значениями nationality
     */
    public Collection<Country> getNationalityDescending() {
        return storage.getAll(true).stream().map((element) -> element.getNationality()).toList();
    }

    /**
     * Фильтрует коллекцию по префиксу passportID
     * @param prefix префикс для фильтрации
     * @return коллекцию элементов, passportID которых начинается с prefix
     */
    public Collection<Person> filterPassportIDPrefix(String prefix) {
        return storage.getAll(false).stream()
                .filter((element) -> element.getPassportID().startsWith(prefix)).toList();
    }
}
