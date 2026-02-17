package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;

import java.util.HashMap;
import java.util.Map;


public class RegistryCommand {
    private final Map<String, BaseCommand> commands = new HashMap<>();

    public void register(String name, BaseCommand command) {
        if (!commands.containsKey(name)) {
            commands.put(name, command);
            return;
        }
        // logging in future
    }

    public void remove(String name) {
        if (commands.containsKey(name)) {
            commands.remove(name);
            return;
        }
        // logging in future
    }

    public BaseCommand get(String commandName) {
        if (commands.containsKey(commandName)) {
            return commands.get(commandName);
        }
        // logging in future
        return null;
    }

    public Map<String, BaseCommand> getMap() {
        return commands;
    }

    public boolean hasCommand(String commandName) {
        return commands.containsKey(commandName);
    }



}