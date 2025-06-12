package com.itmo.nxzage.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.logging.Logger;
import com.itmo.nxzage.common.util.data.DataElement;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;
import com.itmo.nxzage.common.util.serialization.CSVConverter;
import com.itmo.nxzage.server.exceptions.DumpException;
import com.itmo.nxzage.server.exceptions.ReadException;
import com.itmo.nxzage.server.logging.ServerLogger;

// TODO переписать на stream API
public final class Storage<T extends DataElement<T>> {
    private CSVConverter<T> converter;
    private DumpManager dumpManager;
    private TreeSet<T> collection;
    private final Logger logger = ServerLogger.getLogger("Storage");

    public Storage(String filename, CSVConverter<T> converter) {
        this.dumpManager = new DumpManager(filename);
        this.converter = converter;
        // this.dumpManager = new DumpManager(
        // "./lab-server/target/storage_dir/store_file.csv");
        // TODO нашаманить этот IllegalState
        // if (!this.load()) {
        //     throw new IllegalStateException("No file-connection");
        // }
    }

    /**
     * Загружает коллекцию из файла
     * @return true, если удалось 
     */
    public boolean load() {
        return this.load(this.dumpManager);
    }

    private boolean load(DumpManager dumpManager) {
        try {
            String data = dumpManager.read();
            collection = new TreeSet<T>(converter.deserialize(data));
            return true;
        } catch (ReadException exc) {
            return false;
        } catch (CSVParseException exc) {
            collection = new TreeSet<T>();
            return true;
        }
    }

    /**
     * Сохраняет коллекцию в файл
     * @return true, если удалось записать
     */
    public boolean dump() {
        return this.dump(this.dumpManager);
    }

    private boolean dump(DumpManager dumpManager) {
        try {
            String serializedCollection = converter.serialize(collection);
            dumpManager.write(serializedCollection);
        } catch (DumpException exc) {
            // TODO проглатывается exc
            logger.warning("Failed to dump collection");
            return false;
        }
        logger.info("Collection successfully dumped");
        return true;
    }

    /**
     * Добавляет новый элемент в коллекцию
     * @param element новый элемент
     */
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("New element can\'t be null");
        }
        element.validate();
        element.markID();
        collection.add(element);
        logger.info("Element with id=" + element.getID().toString() + " added");
    }

    /**
     * Возвращает элемент с заданным id
     * @param id
     * @return элемент с заданным id, null, если такого не существует
     */
    public T get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id can\'t be null");
        }
        if (id < 0) {
            throw new IllegalArgumentException("Id can\'t be null");
        }
        Iterator<T> iter = collection.iterator();
        while (iter.hasNext()) {
            T elem = iter.next();
            if (elem.getID() == id) {
                return elem;
            }
        }
        return null;
    }

    /**
     * Возвращает полную отсортированную коллекцию в текущем состоянии
     * @param reversed true, если нужно вернуть коллекцию в порядке убывания
     * @return отсортированную коллекцию
     */
    public Collection<T> getAll(boolean reversed) {
        ArrayList<T> list = new ArrayList<T>();
        Iterator<T> iter = collection.iterator();
        while (iter.hasNext()) {
            T element = iter.next();
            if (reversed) {
                list.add(0, element);
            } else {
                list.add(element);
            }
        }

        return list;
    }


    /**
     * Удаляет элемент с заданным id
     * @param id 
     * @return true, если удалось удалить
     */
    public boolean remove(Integer id) {
        T element = this.get(id);
        if (element == null) {
            return false;
        }

        boolean removed = collection.remove(element);
        if (removed) {
            logger.info("Element with id=" + id.toString() + " removed");
        }
        return removed;
    }

    /**
     * Очищает коллекцию
     */
    public void clear() {
        this.collection.clear();
        logger.info("Collection cleared");
    }

    /**
     * Обновляет неавтогенерируемые поля объекта с заданным id в соответсвии с переданным объектом
     * @param id id изменяемого объекта
     * @param newElement новые значения
     * @return true, если удалось изменить
     */
    public boolean update(Integer id, T newElement) {
        if (id == null) {
            throw new IllegalArgumentException("ID can\'t bw null");
        }
        newElement.validate();
        T element = this.get(id);
        if (element == null) {
            return false;
        }

        element.update(newElement);
        logger.info("Element with id=" + id.toString() + " updated");
        return true;
    }

    /**
     * Возвращает служебную информацию о коллекции
     * 
     * @return строка, содержащая дату создания, тип хранимых объектов, количество элементов, id
     *         минимального элемента, id максимального элемента
     */
    public String info() {
        var info = String.format("Collection: TreeSet| {%d} elements | min id: {%d} | max id: {%d}",
         collection.size(), isEmpty() ? -1 : min().getID(), isEmpty() ? -1 : max().getID() );
        return info;
    }

    /**
     * Возвращает минимальный элемент коллекции
     * 
     * @return минимальный элемент
     * @throws NoSuchElementException если коллекция пустая
     */
    public T min() {
        return collection.first();
    }

    /**
     * Возвращает максимальный элемент коллекции
     * 
     * @return максимальный элемент коллекции
     * @return NoSuchElementException если коллекция пустая
     */
    public T max() {
        return collection.last();
    }

    /**
     * Проверка пустоты коллекции
     * 
     * @return true, если коллекция пустая
     */
    public boolean isEmpty() {
        return collection.isEmpty();
    }
}
