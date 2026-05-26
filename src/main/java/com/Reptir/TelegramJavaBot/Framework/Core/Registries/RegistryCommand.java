package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class RegistryCommand {
    private final Map<String, CommandEntry> commands = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RegistryCommand.class);

    public void register(String name, CommandEntry entry) {
        if (commands.containsKey(name)) {
            logger.warn("CommandEntry '{}' already exists, skipping", name);
            return;
        }

        commands.put(name, entry);
        logger.info("CommandEntry '{}' was registered", name);
    }

    public void remove(String name) {
        if (!commands.containsKey(name)) {
            logger.warn("Command '{}' not find, cannot remove", name);
            return;
        }
        logger.info("Command '{}' was removed", name);
        commands.remove(name);
    }

    public CommandEntry get(String commandName) {
        return commands.get(commandName);
    }

    public Map<String, CommandEntry> getMap() {
        return commands;
    }

    public boolean hasCommandEntry(String commandName) {
        return commands.containsKey(commandName);
    }



}