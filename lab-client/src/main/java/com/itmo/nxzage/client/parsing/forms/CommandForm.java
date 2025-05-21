package com.itmo.nxzage.client.parsing.forms;

import java.text.ParseException;
import java.util.Map;
import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.client.commands.CommandHeader;
import com.itmo.nxzage.client.commands.CommandRegister;
import com.itmo.nxzage.client.exceptions.InputSourceHoldConflictException;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.client.parsing.fields.AutoValidatableField;
import com.itmo.nxzage.client.parsing.fields.CommandHeaderField;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

public class CommandForm implements Form<Command> {
    private CommandRegister register;

    public CommandForm(CommandRegister register) {
        this.register = register;
    }

    /**
     * Проверяет существование команды из заголовка в журнале
     * 
     * @param header проверяемый заголовок
     * @throws ValidationException если команда, описанная в заголовке, не может быть распознана
     */
    private void validateHeader(CommandHeader header) {
        if (header.getName() == null) {
            throw new ValidationException("Command name can\'t be null");
        }
        if (!register.exissts(header.getName())) {
            throw new ValidationException(
                    String.format("Command \'%s\' does not exists", header.getName()));
        }
    }

    /**
     * Парсит и валидирует переданную строку аргументов
     * 
     * @return null, если inline-аргумменты для данной команды не предусмотрены и не переданы,
     *         аргументы в формате Map иначе
     * @throws ParseException если не удалось распарсить аргументы
     * @throws ValidationException если аргументы были распознаны, но не валидны
     */
    private Map<String, Object> parseInlinesWithValidation(CommandHeader header)
            throws ParseException {
        AutoValidatableField<Map<String, Object>> parser =
                register.getInlineArgsField(header.getName());
        if (parser == null) {
            if (header.hasInlines()) {
                throw new ParseException(String.format(
                        "Command \'%s\' got no inline arguments, but \'%s\' was given",
                        header.getName(), header.getInlines()), 0);
            } else {
                return null;
            }
        }
        return parser.parseWithValidation(header.getInlines());
    }

    private Map<String, Object> parseStandartsWithValidation(InputManager in, OutputHandler out,
            String name) throws ParseException {
        Form<Map<String, Object>> form = register.getStandartArgsForm(name);
        return (form == null) ? null : form.fill(in, out);
    }

    @Override
    public Command fill(InputManager in, OutputHandler out) throws ParseException {
        CommandHeader header = new CommandHeaderField().parse(in.nextLine().trim());
        validateHeader(header);
        Command command = new Command(header.getName(), register.getType(header.getName()));
        command.applyArgs(parseInlinesWithValidation(header));
        try {
            in.holdSource(this);
            command.applyArgs(parseStandartsWithValidation(in, out, command.getName()));
            return command;
        } catch (InputSourceHoldConflictException exc) {
            throw new ParseException("Input closed unexpectedly, parsing was aborted", 0);
        } finally {
            in.releaseSource(this);
        }
    }
}
