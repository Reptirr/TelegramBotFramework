package com.Reptir.TelegramJavaBot.Framework.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.TriggerLogic.Trigger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandRouter {
    private final RegistryCommand commands;

    public CommandRouter(RegistryCommand commands) {
        this.commands = commands;
    }


    public Set<BaseCommand> getMatched(Context ctx) {
        Set<BaseCommand> resultCommands = new HashSet<>();
        for (Map.Entry<Trigger, BaseCommand> entry : commands.getEntrySet()) {
            if (entry.getKey().match(ctx)) resultCommands.add(entry.getValue());
        }

        return resultCommands;
    }

}
