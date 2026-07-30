package com.Reptir.TelegramJavaBot.Framework.Registries;

import com.Reptir.TelegramJavaBot.Framework.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class RegistryCommand {
    private final Map<Trigger, BaseCommand> commands = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RegistryCommand.class);

    public void register(Trigger trigger, BaseCommand entry) {
        if (Objects.equals(commands.get(trigger), entry)) {
            logger.warn("Entry trigger - command already exists. Skipping");
            return;
        }

        commands.put(trigger, entry);
    }

    public Set<Map.Entry<Trigger, BaseCommand>> getEntrySet() {
        return commands.entrySet();
    }

}