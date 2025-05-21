package com.itmo.nxzage.client;

import java.io.FileNotFoundException;
import java.text.ParseException;
import com.itmo.nxzage.client.commands.Command;
import com.itmo.nxzage.client.commands.CommandRegister;
import com.itmo.nxzage.client.commands.Command.Type;
import com.itmo.nxzage.client.exceptions.InputSourceHoldConflictException;
import com.itmo.nxzage.client.io.ConsoleInput;
import com.itmo.nxzage.client.io.ConsoleOutput;
import com.itmo.nxzage.client.io.FileInput;
import com.itmo.nxzage.client.io.InputManager;
import com.itmo.nxzage.client.io.OutputHandler;
import com.itmo.nxzage.client.parsing.fields.FilenameField;
import com.itmo.nxzage.client.parsing.fields.IDField;
import com.itmo.nxzage.client.parsing.fields.PassportPrefixField;
import com.itmo.nxzage.client.parsing.forms.CommandForm;
import com.itmo.nxzage.client.parsing.forms.PersonArgForm;
import com.itmo.nxzage.client.parsing.forms.PersonForm;
import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.common.util.exceptions.ValidationException;


// TODO СКРИПТЫ

/**
 * Основной класс приложения, который отвечает за его цикл и координирует модули
 */
public class App {
    private InputManager in;
    private OutputHandler out;
    private CommandParser parser;
    private CommandExecutor executor;
    private ExecutionResponsePrinter printer;
    private boolean running = false;

    /**
     * Инициализатор CommandRegister
     * 
     * @return заполненный command register
     */
    private CommandRegister getCR() {
        var CR = new CommandRegister();
        CR.recordCommand("help", Type.CLIENT, null, null);
        CR.recordCommand("execute_script", Type.CLIENT, new FilenameField(), null);
        CR.recordCommand("exit", Type.CLIENT, null, null);
        CR.recordCommand("info", Type.SERVER, null, null);
        CR.recordCommand("show", Type.SERVER, null, null);
        CR.recordCommand("print_field_ascending_nationality", Type.SERVER, null, null);
        CR.recordCommand("print_field_descending_nationality", Type.SERVER, null, null);
        CR.recordCommand("filter_starts_with_passport_id", Type.SERVER, new PassportPrefixField(),
                null);
        CR.recordCommand("add", Type.SERVER, null, new PersonArgForm());
        CR.recordCommand("add_if_max", Type.SERVER, null, new PersonArgForm());
        CR.recordCommand("add_if_min", Type.SERVER, null, new PersonArgForm());
        CR.recordCommand("update", Type.SERVER, new IDField(), new PersonArgForm());
        CR.recordCommand("remove_by_id", Type.SERVER, new IDField(), null);
        CR.recordCommand("remove_lower", Type.SERVER, null, new PersonArgForm());
        CR.recordCommand("clear", Type.SERVER, null, null);
        CR.recordCommand("save", Type.SERVER, null, null);
        return CR;
    }

    private void processCommand(Command command) {
        if (command.isClient()) {
            processClientCommand(command);
        } else if (command.isServer()) {
            processServerCommand(command);
        } else {
            throw new IllegalStateException("Attempt to process unrecognized command");
        }
    }

    private void processServerCommand(Command command) {
        if (!command.isServer()) {
            throw new IllegalArgumentException("Expected server command");
        }

        printer.handle(executor.execute(command));
    }

    private void processClientCommand(Command command) {
        if (!command.isClient()) {
            throw new IllegalArgumentException("Expected client command");
        }

        switch (command.getName()) {
            case "help":
                help();
                break;
            case "execute_script":
                executeScript((String) command.getArgs().get("filename"));
                break;
            case "exit": 
                exit();
                break;
        }
    }

    private void help() {
        String help = "- `help` : вывести справку по доступным командам\n" + //
                        "- `info` : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" + //
                        "- `show` : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" + //
                        "- `add {element}` : добавить новый элемент в коллекцию\n" + //
                        "- `update id {element}` : обновить значение элемента коллекции, id которого равен заданному\n" + //
                        "- `remove_by_id id` : удалить элемент из коллекции по его id\n" + //
                        "- `clear` : очистить коллекцию\n" + //
                        "- `save` : сохранить коллекцию в файл\n" + //
                        "- `execute_script file_name` : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" + //
                        "- `exit` : завершить программу (без сохранения в файл)\n" + //
                        "- `add_if_max {element}` : добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n" + //
                        "- `add_if_min {element}` : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" + //
                        "- `remove_lower {element}` : удалить из коллекции все элементы, меньшие, чем заданный\n" + //
                        "- `filter_starts_with_passport_i_d passportID` : вывести элементы, значение поля passportID которых начинается с заданной подстроки\n" + //
                        "- `print_field_ascending_nationality` : вывести значения поля nationality всех элементов в порядке возрастания\n" + //
                        "- `print_field_descending_nationality` : вывести значения поля nationality всех элементов в порядке убывания";
        out.printSpecial(help + "\n");
    }

    private void executeScript(String filename) {
        try {
            var script = new FileInput(filename);
            in.pushSource(script);
        } catch (InputSourceHoldConflictException exc) {
            out.printError("Unable to execute script: " + exc.getMessage() + "\n");
        } catch (FileNotFoundException exc) {
            out.printMessage("Unable to exceute script: " + exc.getMessage() + "\n");
        } catch (RuntimeException exc) {
            out.printMessage("Unable to exceute script: " + exc.getMessage() + "\n");
        }
    }

    private void exit() {
        out.printMessage("Выполняется выход...\n");
        running = false;
    }

    public void init(com.itmo.nxzage.server.App server) {
        in = new InputManager();
        in.pushSource(new ConsoleInput());
        out = new ConsoleOutput();
        printer = new ExecutionResponsePrinter(out);
        parser = new CommandParser(in, out, getCR());
        executor = new CommandExecutor(server);
    }

    public void run() {
        running = true;
        while (running) {
            processCommand(parser.get());
        }
    }

    // ! test
    public void testCommand() {
        out = new ConsoleOutput();
        in = new InputManager();
        in.pushSource(new ConsoleInput());
        // initCR();
        var form = new CommandForm(getCR());
        while (true) {
            try {
                if (in.isInteractive()) {
                    out.printMessage("Enter command...\n");
                }
                Command command = form.fill(in, out);
                out.printMessage(command.toString() + "\n");
            } catch (ParseException exc) {
                out.printError("[PARSING]" + exc.getMessage() + "\n");
            } catch (ValidationException exc) {
                out.printError("[VALIDATION]" + exc.getMessage() + "\n");
            }
        }
    }

    // ! test
    public void testPersonIn() {
        out = new ConsoleOutput();
        in = new InputManager();
        in.pushSource(new ConsoleInput());
        while (true) {
            try {
                Person p = (new PersonForm()).fill(in, out);
                out.printMessage(p.toString() + "\n");
            } catch (ParseException exc) {
                out.printError("[FORMAT ERROR]" + exc.getMessage() + "\n");
            } catch (ValidationException exc) {
                out.printError("[VALIDATION ERROR]" + exc.getMessage() + "\n");
            }
        }
    }

    // ! test
    public void testOut() {
        out = new ConsoleOutput();
        out.enablePrompts();
        out.printMessage("Pizdec ura\n");
        out.printMessage("LOL PENIS\n");
        out.printMessage("\n");
        out.printPrompt("Vvedin slovo \"pizsda\"");
        out.printMessage("\n");
        out.printPrompt("");
        out.printMessage("\n");
        out.printError("Error\n");
    }
}
