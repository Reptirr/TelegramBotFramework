package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandEntry;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public Set<BaseCommand> getMatched(Context ctx) {
        Set<BaseCommand> resultCommands = new HashSet<>();
        for (Map.Entry<Trigger, CommandEntry> entry : commands.entrySet()) {
            if (entry.getKey().match(ctx)) resultCommands.add(entry.getValue().command());
        }

        return resultCommands;
    }

}