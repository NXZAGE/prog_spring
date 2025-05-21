package com.itmo.nxzage.client.parsing.fields;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itmo.nxzage.client.commands.CommandHeader;

/**
 * Поле, которое выполняет парсинг строки с именем команды
 */
public class CommandHeaderField implements Field<CommandHeader> {
    private static Pattern PATTERN;

    static {
        PATTERN = Pattern.compile("([a-z_]+)(?:\\s+(\\S.*))?");
    }

    @Override
    public String getPrompt() {
        return null;
    }

    @Override
    public CommandHeader parse(String line) throws ParseException {
        Matcher matcher;
        if ((matcher = PATTERN.matcher(line.trim())).matches()) {
            String name = matcher.group(1);
            String inlines = matcher.group(2);
            if (inlines != null && inlines.isBlank()) {
                inlines = null;
            }
            return new CommandHeader(name, inlines);
        }
        throw new ParseException(
                "Unable to recognize command header. Follow pattern [command_name (args...)?].", 0);
    }

}
