package com.itmo.nxzage.common.util.data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.common.util.exceptions.CSVParseException;
import com.itmo.nxzage.common.util.exceptions.ValidationException;
import com.itmo.nxzage.common.util.serialization.CSVConvertable;

/**
 * Основной класс информации о человеке
 */
public class Person extends DataElement<Person> {
    // Статические поля для сериализации/десериализации CSV
    private static Integer nextID = 1;
    private static Pattern CSV_DESERIALIZATION_PATTERN;
    private static String CSV_SERIALIZATION_PATTERN;

    /**
     * Формат даты для поля creationDate
     */
    public static final DateFormat CREATION_DATE_FORMAT =
            new SimpleDateFormat("<dd.MM.yyyy HH:mm:ss z>");

    // Инициализация паттернов для CSV
    static {
        CSV_SERIALIZATION_PATTERN = "%d" + CSVConvertable.DELIMETER + // id
                "%s" + CSVConvertable.DELIMETER + // creationDate
                "%s" + CSVConvertable.DELIMETER + // name
                "%s" + CSVConvertable.DELIMETER + // coordinates
                "%f" + CSVConvertable.DELIMETER + // height
                "%d" + CSVConvertable.DELIMETER + // weight
                "%s" + CSVConvertable.DELIMETER + // passportID
                "%s" + CSVConvertable.DELIMETER + // nationality
                "%s"; // location

        CSV_DESERIALIZATION_PATTERN = Pattern.compile("^(.+)" + DELIMETER_ESCAPE + "(.+)"
                + DELIMETER_ESCAPE + "(.+)" + DELIMETER_ESCAPE + "(.+)" + DELIMETER_ESCAPE + "(.+)"
                + DELIMETER_ESCAPE + "(.+)" + DELIMETER_ESCAPE + "(.+)" + DELIMETER_ESCAPE + "(.+)"
                + DELIMETER_ESCAPE + "(.+)$");
    }

    // Поля класса
    /**
     * Уникальный идентификатор записи (автогенерируемый, >0)
     */
    private Integer id;

    /**
     * Дата создания записи (автогенерируемая, не null)
     */
    private Date creationDate;

    /**
     * Имя человека (не null, не пустая строка, не содержит DELIMITER)
     */
    private String name;

    /**
     * Координаты человека (не null)
     */
    private Coordinates coordinates;

    /**
     * Рост человека (>0)
     */
    private Float height;

    /**
     * Вес человека (>0)
     */
    private Long weight;

    /**
     * Номер паспорта (длина 5-24 символа, может быть null)
     */
    private String passportID;

    /**
     * Страна (не null)
     */
    private Country nationality;

    /**
     * Местоположение (не null)
     */
    private Location location;

    /**
     * Приватный конструктор для внутреннего использования
     * 
     * @param id уникальный идентификатор
     * @param creationDate дата создания
     * @param name имя
     * @param coordinates координаты
     * @param height рост
     * @param weight вес
     * @param passportID номер паспорта
     * @param nationality страна
     * @param location местоположение
     */
    private Person(Integer id, Date creationDate, String name, Coordinates coordinates,
            Float height, Long weight, String passportID, Country nationality, Location location) {
        this.id = id;
        this.creationDate = creationDate;
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
        this.nationality = nationality;
        this.location = location;
    }

    /**
     * Основной конструктор для создания нового объекта Person
     * 
     * @param name имя
     * @param coordinates координаты
     * @param height рост
     * @param weight вес
     * @param passportID номер паспорта
     * @param nationality страна
     * @param location местоположение
     */
    public Person(String name, Coordinates coordinates, Float height, Long weight,
            String passportID, Country nationality, Location location) {
        this.id = 0;
        this.creationDate = new Date();
        this.name = name;
        this.coordinates = coordinates;
        this.height = height;
        this.weight = weight;
        this.passportID = passportID;
        this.nationality = nationality;
        this.location = location;
    }

    public Person() {
        this.id = 0;
        this.creationDate = new Date();
        this.name = null;
        this.coordinates = null;
        this.height = 0F;
        this.weight = 0L;
        this.passportID = null;
        this.nationality = null;
        this.location = null;
    }

    /**
     * Обновляет значение следующего доступного ID
     * 
     * @param collection коллекция объектов Person
     */
    public static void updateNextID(Collection<Person> collection) {
        Integer maxID = 0;
        for (Person element : collection) {
            maxID = Integer.max(maxID, element.id);
        }
        nextID = maxID + 1;
    }

    /**
     * Десериализует объект Person из CSV-строки
     * 
     * @param element CSV-строка
     * @return новый объект Person
     * @throws CSVParseException при ошибках парсинга
     */
    public Person deserializeCSV(String element) throws CSVParseException {
        // TODO Вынести логику в отдельный класс
        Matcher matcher = CSV_DESERIALIZATION_PATTERN.matcher(element);
        if (matcher.find()) {
            try {
                Integer id = Integer.parseInt(matcher.group(1));
                Date creationDate = CREATION_DATE_FORMAT.parse(matcher.group(2));
                String name = matcher.group(3);
                Coordinates coordinates = new Coordinates().deserializeCSV(matcher.group(4)); // TODO fixit
                Float height = Float.parseFloat(matcher.group(5));
                Long weight = Long.parseLong(matcher.group(6));
                String passportID = matcher.group(7);
                Country nationality = Country.JAPAN.deserializeCSV(matcher.group(8)); // TODO fixit
                Location location = new Location().deserializeCSV(matcher.group(9)); // TODO fixit
                return new Person(id, creationDate, name, coordinates, height, weight, passportID,
                        nationality, location);
            } catch (ParseException exc) {
                throw new CSVParseException("Parsing failed: " + exc.getMessage(), exc.getCause());
            }
        }
            throw new CSVParseException("CSV string doesn\'t match the pattern");
    }

    @Override
    public void validate() throws ValidationException {
        PersonValidator.validate(this);
    }

    /**
     * Сериализует объект Person в CSV-строку
     * 
     * @return CSV-строка
     */
    @Override
    public String serializeCSV() {
        // TODO вынести логику в отдельный класс
        return String.format(CSV_SERIALIZATION_PATTERN, id,
                CREATION_DATE_FORMAT.format(creationDate), name, coordinates.serializeCSV(), height,
                weight, passportID, nationality.serializeCSV(), location.serializeCSV());
    }

    @Override
    /**
     * Добавляет актуальный ID для коллекции
     */
    public void markID() {
        id = nextID++;
    }

    // Геттеры и сеттеры
    /**
     * Возвращает уникальный идентификатор
     * 
     * @return ID объекта
     */
    public Integer getID() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор
     * 
     * @param id новое значение ID
     */
    public void setID(Integer id) {
        this.id = id;
    }

    /**
     * Возвращает дату создания
     * 
     * @return дата создания
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Устанавливает дату создания
     * 
     * @param creationDate новая дата создания
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Возвращает имя
     * 
     * @return имя
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает имя
     * 
     * @param name новое имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает координаты
     * 
     * @return координаты
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Устанавливает координаты
     * 
     * @param coordinates новые координаты
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * Возвращает рост
     * 
     * @return рост
     */
    public Float getHeight() {
        return height;
    }

    /**
     * Устанавливает рост
     * 
     * @param height новый рост
     */
    public void setHeight(Float height) {
        this.height = height;
    }

    /**
     * Возвращает вес
     * 
     * @return вес
     */
    public Long getWeight() {
        return weight;
    }

    /**
     * Устанавливает вес
     * 
     * @param weight новый вес
     */
    public void setWeight(Long weight) {
        this.weight = weight;
    }

    /**
     * Возвращает номер паспорта
     * 
     * @return номер паспорта
     */
    public String getPassportID() {
        return passportID;
    }

    /**
     * Устанавливает номер паспорта
     * 
     * @param passportID новый номер паспорта
     */
    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    /**
     * Возвращает страну
     * 
     * @return страна
     */
    public Country getNationality() {
        return nationality;
    }

    /**
     * Устанавливает страну
     * 
     * @param nationality новая страна
     */
    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    /**
     * Возвращает местоположение
     * 
     * @return местоположение
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Устанавливает местоположение
     * 
     * @param location новое местоположение
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Обновляет значения полей объекта на основе другого объекта Person
     * 
     * @param element объект с новыми данными
     */
    public void update(Person element) {
        if (element == null) {
            throw new IllegalArgumentException("Can\'t update element using null");
        }
        this.name = element.name;
        this.coordinates = element.coordinates;
        this.height = element.height;
        this.weight = element.weight;
        this.passportID = element.passportID;
        this.nationality = element.nationality;
        this.location = element.location;
        // TODO validate update
    }

    /**
     * Сравнивает объекты Person
     * 
     * @param other объект для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Person other) {
        if (this.passportID.compareTo(other.passportID) == 0) {
            return this.id.compareTo(other.id);
        }
        return this.passportID.compareTo(other.passportID);
    }

    /**
     * Возвращает строковое представление объекта
     * 
     * @return строковое представление
     */
    @Override
    public String toString() {
        String formattedCreationDate = Person.CREATION_DATE_FORMAT.format(creationDate);
        return String.format(
                "OBJECT PERSON [\n" + "  ID: %d\n" + "  Creation date: %s\n" + "  Name: %s\n"
                        + "  Coordinates: %s\n" + "  Height: %f cm\n" + "  Weight: %d kg\n"
                        + "  Passport ID: %s\n" + "  Nationality: %s\n" + "  Location: %s\n" + "]",
                id, formattedCreationDate, name, coordinates.toString(), height, weight, passportID,
                nationality.toString(), location.toString());
    }

    /**
     * Проверяет равенство объектов
     * 
     * @param other объект для сравнения
     * @return true, если объекты равны
     */
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Person))
            return false;
        Person person = (Person) other;
        return id.equals(person.id) && creationDate.equals(person.creationDate)
                && name.equals(person.name) && coordinates.equals(person.coordinates)
                && height.equals(person.height)
                && (weight == null ? person.weight == null : weight.equals(person.weight))
                && (passportID == null ? person.passportID == null
                        : passportID.equals(person.passportID))
                && nationality.equals(person.nationality) && location.equals(person.location);
    }

    /**
     * Возвращает хэш-код объекта
     * 
     * @return хэш-код
     */
    @Override
    public int hashCode() {
        final int mod = 31;
        int hash = mod;
        hash = hash * mod + (id != null ? id.hashCode() : 0);
        hash = hash * mod + (creationDate != null ? creationDate.hashCode() : 0);
        hash = hash * mod + (name != null ? name.hashCode() : 0);
        hash = hash * mod + (coordinates != null ? coordinates.hashCode() : 0);
        hash = hash * mod + (height != null ? height.hashCode() : 0);
        hash = hash * mod + (weight != null ? weight.hashCode() : 0);
        hash = hash * mod + (passportID != null ? passportID.hashCode() : 0);
        hash = hash * mod + (nationality != null ? nationality.hashCode() : 0);
        hash = hash * mod + (location != null ? location.hashCode() : 0);
        return hash;
    }
}