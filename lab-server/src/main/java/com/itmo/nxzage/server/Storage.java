package com.itmo.nxzage.server;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.util.stream.Stream;
import com.itmo.nxzage.common.util.data.DataElement;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;
import com.itmo.nxzage.common.util.serialization.CSVConverter;
import com.itmo.nxzage.server.exceptions.DumpException;
import com.itmo.nxzage.server.exceptions.ReadException;
import com.itmo.nxzage.server.logging.ServerLogger;
import com.itmo.nxzage.server.services.db.dao.Dao;

// TODO переписать на stream API
public final class Storage<T extends DataElement<T>> {
    private CSVConverter<T> converter;
    private Dao<T> dao;
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
     * constuctor for storage powered by DB
     * 
     */ 
    public Storage(Dao<T> dao) {
        this.dao = dao;
    }

    /**
     * Загружает коллекцию из файла
     * @return true, если удалось 
     */
    public boolean load() {
        return this.loadFromDB();
    }

    private boolean loadFromDB() {
        try {
            List<T> elements = dao.getAll();
            if (elements == null) {
                logger.severe("DB returned null-list of all data");
                return false;
            }
            collection = new TreeSet<T>(elements);
            logger.info("Data from the DB successfully loaded. Currents collection size: " + collection.size());
            return true;
        } catch (SQLException e) {
            logger.severe("SQL Exception happened duiring loading data from DB.");
            e.printStackTrace();
            logger.severe("Failed to load data from the DB. Collcetion is null.");
            return false;
        }
    }

    private boolean load(DumpManager dumpManager) {
        try {
            String data = dumpManager.read();
            collection = new TreeSet<T>(converter.deserialize(data));
            logger.info("Collection deserialized from files. Objects count: " + String.valueOf(collection.size()));
            return true;
        } catch (ReadException exc) {
            logger.warning("Failed to load collection: " + exc.getMessage());
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
        // * UNUSED
        return this.dump(this.dumpManager);
    }

    // * UNUSED
    private boolean dump(DumpManager dumpManager) {
        try {
            String serializedCollection = converter.serialize(collection);
            logger.info("Serialized collection ready to dump. Size: " + serializedCollection.length());
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
        logger.info("Element " + element.toString() + " is valid and ready to be added to the DB");
        try {
            Integer id = dao.save(element);
            var newElement = dao.get(id).orElseThrow(() -> 
                new IllegalStateException("Added element wasn't find")
            );
            collection.add(newElement);
            logger.info("Element with id=" + element.getID().toString() + " added");
        } catch (SQLException e) {
            logger.info("Failed to save the elemtn to the DB: SQL exception orrued: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to save the element:", e);
        }
        // logger.info("Element with id=" + element.getID().toString() + " added");
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
            if (elem.getID().equals(id)) {
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
    public Stream<T> getAll(boolean reversed) {
        if (reversed) {
            return collection.descendingSet().stream();
        } else {
            return collection.stream();
        }
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
        try {
            boolean deleted = dao.delete(id);
            if (deleted) {
                if (!collection.remove(element)) {
                    logger.severe("Element with id=" + id.toString() + "was removed from DB, but not from the collection.");
                    throw new Error("Data integrity was violated");
                }
                logger.info("Element with id=" + id.toString() + " removed");
            } else {
                logger.info("Element with id=" + id.toString() + " was not removed");
            }
            return deleted;
        } catch (SQLException e) {
            logger.info("SQLException occured duiring attempt of the removing element with id=" + id.toString());
            e.printStackTrace();
            return false;
        }
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
        logger.info("New value of element with id=" + id.toString() + " is correct.");
        try {
            dao.update(id, newElement);
            element.update(newElement);
            logger.info("Element with id=" + id.toString() + " successfully updated");
            return true;
        } catch (SQLException e) {
            logger.info("SQL Excpetion occured: failed update element.");
            e.printStackTrace();
            return false;
        }
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
