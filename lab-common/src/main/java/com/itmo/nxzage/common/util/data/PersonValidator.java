package com.itmo.nxzage.common.util.data;

import java.util.Date;
import com.itmo.nxzage.common.util.exceptions.ValidationException;
import com.itmo.nxzage.common.util.serialization.CSVConvertable;

public class PersonValidator {
    // Константы для валидации
    private static final Float MIN_HEIGHT = 1f;
    private static final Long MIN_WEIGHT = 1L;
    private static final Integer PASSPORT_ID_MIN_LENGTH = 5;
    private static final Integer PASSPORT_ID_MAX_LENGTH = 24;

    /**
     * Комплексная валдация всех полей объекта Person
     * 
     * @param person Объект Person для валидации
     * @throws ValidationException при обнаружении некорректных данных
     * @see #validateID(Integer)
     * @see #validateCreationDate(Date)
     * @see #validateName(String)
     * @see #validateCoordinates(Coordinates)
     * @see #validateHeight(Float)
     * @see #validateWeight(Long)
     * @see #validatePassportID(String)
     * @see #validateNationality(Country)
     * @see #validateLocation(Location)
     */
    public static void validate(Person person) throws ValidationException {
        validateID(person.getID());
        validateCreationDate(person.getCreationDate());
        validateName(person.getName());
        validateCoordinates(person.getCoordinates());
        validateHeight(person.getHeight());
        validateWeight(person.getWeight());
        validatePassportID(person.getPassportID());
        validateNationality(person.getNationality());
        validateLocation(person.getLocation());
    }

    /**
     * Валидация уникального идентификатора
     * 
     * @param id Идентификатор для проверки
     * @throws ValidationException если ID не соответствует требованиям
     * @see Person#id
     */
    public static void validateID(Integer id) throws ValidationException {
        if (id == null) {
            throw new ValidationException("ID cannot be null");
        }
        if (id < 0) {
            throw new ValidationException("ID must be a non-negative number");
        }
    }

    /**
     * Валидация даты создания
     * 
     * @param creationDate Дата создания для проверки
     * @throws IllegalArgumentException если дата не соответствует требованиям
     * @see Person#creationDate
     */
    public static void validateCreationDate(Date creationDate) throws ValidationException {
        if (creationDate == null) {
            throw new ValidationException("Creation date cannot be null");
        }
        if (creationDate.compareTo(new Date()) > 0) {
            throw new ValidationException("Creation date cannot be in the future");
        }
    }

    /**
     * Валидация имени
     * 
     * @param name Имя для проверки
     * @throws IllegalArgumentException если имя не соответствует требованиям
     * @see Person#name
     */
    public static void validateName(String name) throws ValidationException {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("Name cannot be null or empty");
        }
        if (name.contains(CSVConvertable.DELIMETER)) {
            throw new ValidationException("Name cannot contain a delimiter");
        }
        if (name.contains(CSVConvertable.STRING_DELIMETER)) {
            throw new ValidationException("Name cannot contain a string delimiter character");
        }
    }

    /**
     * Валидация координат
     * 
     * @param coordinates Координаты для проверки
     * @throws IllegalArgumentException если координаты не соответствуют требованиям
     * @see Person#coordinates
     */
    public static void validateCoordinates(Coordinates coordinates) throws ValidationException {
        if (coordinates == null) {
            throw new ValidationException("Coordinates cannot be null");
        }
    }

    /**
     * Валидация роста
     * 
     * @param height Рост для проверки
     * @throws ValidationException если рост не соответствует требованиям
     * @see Person#height
     */
    public static void validateHeight(Float height) throws ValidationException {
        if (height == null) {
            throw new ValidationException("Height cannot be null");
        }
        if (height <= MIN_HEIGHT) {
            throw new ValidationException("Height must be greater than " + MIN_HEIGHT);
        }
    }

    /**
     * Валидация веса
     * 
     * @param weight Вес для проверки
     * @throws ValidationException если вес не соответствует требованиям
     * @see Person#weight
     */
    public static void validateWeight(Long weight) throws ValidationException {
        if (weight != null && weight <= MIN_WEIGHT) {
            throw new ValidationException("Weight must be greater than " + MIN_WEIGHT);
        }
    }

    /**
     * Валидация номера паспорта
     * 
     * @param passportID Номер паспорта для проверки
     * @throws ValidationException если номер паспорта не соответствует требованиям
     * @see Person#passportID
     */
    public static void validatePassportID(String passportID) throws ValidationException {
        if (passportID != null) {
            if (passportID.length() < PASSPORT_ID_MIN_LENGTH) {
                throw new ValidationException("Passport ID must be at least "
                        + PASSPORT_ID_MIN_LENGTH + " characters long");
            }
            if (passportID.length() > PASSPORT_ID_MAX_LENGTH) {
                throw new ValidationException(
                        "Passport ID cannot exceed " + PASSPORT_ID_MAX_LENGTH + " characters");
            }
            if (passportID.contains(CSVConvertable.DELIMETER)) {
                throw new ValidationException("Passport ID cannot contain a delimiter");
            }
            if (passportID.contains(CSVConvertable.STRING_DELIMETER)) {
                throw new ValidationException(
                        "Passport ID cannot contain a string delimiter character");
            }
        }
    }

    /**
     * Валидация страны
     * 
     * @param nationality Страна для проверки
     * @throws IllegalArgumentException если страна не соответствует требованиям
     * @see Person#nationality
     */
    public static void validateNationality(Country nationality) throws ValidationException {
        if (nationality == null) {
            throw new ValidationException("Nationality cannot be null");
        }
    }

    /**
     * Валидация местоположения
     * 
     * @param location Местоположение для проверки
     * @throws ValidationException если местоположение не соответствует требованиям
     * @see Person#location
     */
    public static void validateLocation(Location location) throws ValidationException {
        if (location == null) {
            throw new ValidationException("Location cannot be null");
        }
        if (location.getName().contains(CSVConvertable.DELIMETER)) {
            throw new ValidationException("Location name cannot contain a delimiter");
        }
        if (location.getName().contains(CSVConvertable.STRING_DELIMETER)) {
            throw new ValidationException(
                    "Location name cannot contain a string delimiter character");
        }
    }

}
