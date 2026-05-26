package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class RegistryCommand {
    private final Map<String, BaseCommand> commands = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RegistryCommand.class);

    private BaseCommand defaultCommand;

    public void register(String name, BaseCommand command) {
        if (commands.containsKey(name)) {
            logger.warn("CommandEntry '{}' already exists, skipping", name);
            return;
        }

        commands.put(name, command);
        logger.info("CommandEntry '{}' was registered", name);
    }

    public void registerDefaultCommand(BaseCommand command) {
        defaultCommand = command;
        logger.info("Default command was registered");
    }

    public void remove(String name) {
        if (!commands.containsKey(name)) {
            logger.warn("Command '{}' not find, cannot remove", name);
            return;
        }
        logger.info("Command '{}' was removed", name);
        commands.remove(name);
    }

    public BaseCommand getDefaultCommand() {
        return defaultCommand;
    }

    public BaseCommand get(String commandName) {
        return commands.get(commandName);
    }

    public Map<String, BaseCommand> getMap() {
        return commands;
    }

    public boolean hasCommand(String commandName) {
        return commands.containsKey(commandName);
    }



}