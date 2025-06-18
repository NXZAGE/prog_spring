package com.itmo.nxzage.common.util.data;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

/**
 * Универсальный контейнер данных, передаваемых в запросах и ответах. Обеспечивает типобезопасный
 * доступ, гибкую валидацию и сериализацию.
 */
public class DataContainer implements Serializable, Validatable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Object> data;
    private transient Consumer<DataContainer> validator;

    {
        data = new HashMap<String, Object>();
        validator = dc -> {};
    }

    public DataContainer put(String key, Object value) {
        data.put(Objects.requireNonNull(key), value);
        return this;
    }

    public DataContainer putAll(DataContainer other) {
        if (other == null) return this;
        this.data.putAll(other.data);
        return this;
    }

    public <T> T get(String key, Class<T> type) {
        Object value = data.get(key);
        if (value == null) {
            throw new ValidationException("Missing required key: " + key);
        }
        if (!type.isInstance(value)) {
            throw new ValidationException(
                    "Key '" + key + "' must be of type " + type.getSimpleName());
        }
        return type.cast(value);
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public void requireKeys(String... keys) {
        for (String key : keys) {
            if (!data.containsKey(key)) {
                throw new ValidationException("Missing required key: " + key);
            }
        }
    }

    public void assertType(String key, Class<?> expectedType) {
        Object value = data.get(key);
        if (value == null) {
            throw new ValidationException("Missing required key: " + key);
        }
        if (!expectedType.isInstance(value)) {
            throw new ValidationException(
                    "Key '" + key + "' must be of type " + expectedType.getSimpleName());
        }
    }

    // Установка кастомной логики валидации
    public void setValidator(Consumer<DataContainer> validator) {
        if (validator == null) {
            throw new IllegalArgumentException("Validator cannot be null");
        }

        this.validator = validator;
    }

    @Override
    public void validate() throws ValidationException {
        validator.accept(this);
    }

    // Утилиты
    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public Set<String> keys() {
        return Collections.unmodifiableSet(data.keySet());
    }

    public Map<String, Object> asMap() {
        return Collections.unmodifiableMap(data);
    }

    @Override
    public String toString() {
        return "DataContainer" + data;
    }
}
