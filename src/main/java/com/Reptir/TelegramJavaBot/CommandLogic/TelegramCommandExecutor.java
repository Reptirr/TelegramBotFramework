package com.Reptir.TelegramJavaBot.CommandLogic;

/*
* Имеет лист из команд, имеет функ ExecCommand которая запускает команду из аргумента
*/

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class TelegramCommandExecutor {
    private final Map<String, Command> commands = new HashMap<>();

    public void AddCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public void DelCommand(Command command) {
        commands.remove(command.getName());
    }

    private void ExecCommand(String commandName, InformationForCommand information, String[] args)  {
        if (commands.containsKey(commandName)) {
            Command command = commands.get(commandName);
            command.execute(information, args);
        }
    }

    public void ExecByInternal(String commandName, InformationForCommand information, String[] args)  {
        ExecCommand(commandName, information, args);
    }

    public void ExecByInput(String commandName, InformationForCommand information, String[] args)  {
        if (commands.get(commandName).isForUserInput()) {
            ExecCommand(commandName, information, args);
        }
    }
}
