package com.itmo.nxzage.common.util.data;

import java.io.Serializable;
import com.itmo.nxzage.common.util.serialization.CSVConvertable;

public abstract class DataElement<T extends DataElement<T>> implements Validatable, CSVConvertable<T>, Serializable, Comparable<T> {
    protected Integer id;

    /**
     * Обновляет неавтогенерируемые поля объекта
     * @param element источник данных
     */
    public abstract void update(T element);

    public Integer getID() {
        return id;
    }

    public abstract void markID();
}
