package com.itmo.nxzage.common.util.data;

import com.itmo.nxzage.common.util.exceptions.ValidationException;
// TODO возможно ValidationException не надо пробрасывать в throw, т.к. это рантайм
/**
 * Интерфейс для валидируемых типов
 */
public interface Validatable {
    /**
     * Запускает валидацию
     * <p>Резлуьтат положительный, если в процессе не возникло исключений </p>
     * @throws ValidationException при обнуружении невалидных элементов
     */
    public void validate() throws ValidationException;
    /**
     * 
     * @return true, если объект валидный, иначе false
     */
    public default boolean isValid() {
        try {
            validate();
            return true;
        } catch (ValidationException exception) {
            return false;
        }
    }
}
