package com.itmo.nxzage.client;

import java.text.ParseException;
import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.client.commands.CommandRegister;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.client.parsing.forms.CommandForm;
import com.itmo.nxzage.common.util.exceptions.ValidationException;

/**
 * Класс, который считывает команды
 */
public class CommandParser {
    private InputManager in;
    private OutputHandler out;
    private CommandForm form;

    public CommandParser(InputManager in, OutputHandler out, CommandRegister CR) {
        this.in = in;
        this.out = out;
        form = new CommandForm(CR);
    }

    /**
     * Пытается считать очередную команду, пока не получится
     * @return очередную команду
     */
    public Command get() {
        while (true) {
            try {
                if (in.isInteractive()) {
                    out.printMessage("Enter command...\n");
                }
                return form.fill(in, out);
            } catch (ParseException exc) {
                out.printError("[PARSING]" + exc.getMessage() + "\n");
            } catch (ValidationException exc) {
                out.printError("[VALIDATION]" + exc.getMessage() + "\n");
            } catch (RuntimeException exc) {
                out.printError("Unhandled exception...\n");
            }
        }
    }
}
