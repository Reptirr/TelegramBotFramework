package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandEntry;
import com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;


public class RegistryCommand {
    private final Map<Trigger, CommandEntry> commands = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RegistryCommand.class);

    public void register(Trigger trigger, CommandEntry entry) {
        if (Objects.equals(commands.get(trigger), entry)) {
            logger.warn("Entry trigger - command already exists. Skipping");
            return;
        }

        commands.put(trigger, entry);
    }

    public Set<BaseCommand> get(Update update) {
        Set<BaseCommand> resultCommands = new HashSet<>();
        for (Map.Entry<Trigger, CommandEntry> entry : commands.entrySet()) {
            if (entry.getKey().match(update)) resultCommands.add(entry.getValue().command());
        }

        return resultCommands;
    }

}