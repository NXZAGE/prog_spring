package com.itmo.nxzage.client.commands;

import java.util.HashMap;
import java.util.Map;

/**
 * Клиентский класс комамнды
 * <p>
 * Содержит <b>имя</b>, <b>тип</b> и <b>аргументы</b>
 * </p>
 */
public class Command {
    public static enum Type {
        CLIENT, SERVER;
    }

    private String name;
    private Type type;
    private Map<String, Object> args;

    public Command(String name, Type type) {
        this.name = name;
        this.type = type;
        this.args = new HashMap<String, Object>();
    }

    /**
     * Добавляет аргументы команды <i>(merge maps)</i>
     * <p>
     * Попытка перезаписи существующих аргументов отклоняется
     * </p>
     * 
     * @param args новые аргументы
     */
    public void applyArgs(Map<String, Object> args) {
        if (args == null) {
            return;
        }
        args.forEach((key, value) -> this.args.merge(key, value, (oldV, newV) -> oldV));
    }

    public boolean isClient() {
        return type.equals(Type.CLIENT);
    }

    public boolean isServer() {
        return type.equals(Type.SERVER);
    }

    // * Getters and settters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Map<String, Object> getArgs() {
        return args;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<String, Object>();
        args.forEach((key, value) -> result.merge(key, value, (oldV, newV) -> oldV));
        result.put("name", name);
        return result;
    }

    @Override
    public String toString() {
        // TODO переписать это уродство
        var argsPrinted = new StringBuilder();
        args.forEach((key, value) -> argsPrinted
                .append(String.format("    \"%s\": %s;\n", key, value.toString())));
        String result = String.format("Command {\n  name: %s;\n  type: %s;\n  args:  {\n %s} }\n",
                name, type.toString(), argsPrinted.toString());

        return result;
    }
}
