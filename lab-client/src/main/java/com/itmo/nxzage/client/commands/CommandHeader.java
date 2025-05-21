package com.itmo.nxzage.client.commands;

/**
 * Класс для инициализации команды по первой строке пользователя
 * <p>
 * Содержит <i>имя</i>, <i>тип</i> и <i>inline-аргументы</i>, которые предстоит распарсить
 * </p>
 */
public class CommandHeader {
    private String name;
    private String inlines;

    public CommandHeader(String name, String inlines) {
        this.name = name;
        this.inlines = inlines;
    }

    public String getName() {
        return name;
    }

    public String getInlines() {
        return inlines;
    }

    public boolean hasInlines() {
        if (inlines == null || inlines.isBlank()) {
            return false;
        }
        return true;
    }
}
