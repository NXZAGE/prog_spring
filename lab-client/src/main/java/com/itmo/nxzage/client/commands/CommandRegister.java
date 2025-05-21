package com.itmo.nxzage.client.commands;

import java.util.HashMap;
import java.util.Map;
import com.itmo.nxzage.client.commands.Command.Type;
import com.itmo.nxzage.client.parsing.fields.AutoValidatableField;
import com.itmo.nxzage.client.parsing.forms.Form;

/**
 * Класс-журнал для идентификации команд по имени
 * <p>
 * Позволяет получить поле для парсинга строки-заголовка
 * </p>
 * <p>
 * По имени позволяет получить <b>тип</b>, <b>парсер inline-аргументов</b> и <b>форму</b> для
 * стандартных аргументов </>
 */
public class CommandRegister {
    // ? Возможно рекорды хуйня и нужно генерить новые формы/поля на каждый гет, либо хранить в
    // ? Хранить классы, а не объекты
    // рекорде класс
    // * КОНТРАКТ: автовалидируемые обработчики инлайнов
    // * КОНТРАКТ: null, если нет аргументов 
    private static record CommandProperties(Type type, AutoValidatableField<Map<String, Object>> inlineArgsField,
            Form<Map<String, Object>> standartArgsForm) {
    };


    private Map<String, CommandProperties> register;

    public CommandRegister() {
        register = new HashMap<String, CommandProperties>();
    }

    /**
     * Добавляет команду в журнал
     * @param name имя команды
     * @param type тип команды 
     * @param inliner обработчик inline-аргументов (null, если их нет)
     * @param form форма для обработки стандартных аргументов (null, если их нет)
     */
    public void recordCommand(
            String name, 
            Type type, 
            AutoValidatableField<Map<String, Object>> inliner,
            Form<Map<String, Object>> form
        ) {
        register.put(name, new CommandProperties(type, inliner, form));
    }

    /**
     * Проверяет существование команды по имени
     * @param commandNanme имя команды
     * @return true, если команда с переданным именем зарегистрирована, false иначе
     */
    public boolean exissts(String commandName) {
        return register.containsKey(commandName);
    }

    /**
     * @param name имя команды
     * @return тип команды (клиетская/серверная)
     */
    public Type getType(String name) {
        return register.get(name).type();
    }

    /**
     * @param name имя команды
     * @return парсер inline-аргументов команды
     */
    public AutoValidatableField<Map<String, Object>> getInlineArgsField(String name) {
        return register.get(name).inlineArgsField();
    }

    /**
     * @param name имя команды
     * @return форма для парсинга стандартных аргументов команды
     */
    public Form<Map<String, Object>> getStandartArgsForm(String name) {
        return register.get(name).standartArgsForm();
    }
}
