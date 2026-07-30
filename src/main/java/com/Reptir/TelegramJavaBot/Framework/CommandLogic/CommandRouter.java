package com.Reptir.TelegramJavaBot.Framework.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.TriggerLogic.Trigger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandRouter {
    private final RegistryCommand commands;

    public CommandRouter(RegistryCommand commands) {
        this.commands = commands;
    }


    public Set<BaseCommand> getMatched(Update update) {
        Set<BaseCommand> resultCommands = new HashSet<>();
        for (Map.Entry<Trigger, BaseCommand> entry : commands.getEntrySet()) {
            if (entry.getKey().match(update)) resultCommands.add(entry.getValue());
        }

        return resultCommands;
    }

}
