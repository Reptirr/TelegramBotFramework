package com.Reptir.Tafabo.Framework.CommandLogic;

import com.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import com.Reptir.Tafabo.Framework.TriggerLogic.Trigger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandRouter<U, M> {
    private final RegistryCommand<U, M> commands;

    public CommandRouter(RegistryCommand<U, M> commands) {
        this.commands = commands;
    }


    public Set<BaseCommand<U, M>> getMatched(U update) {
        Set<BaseCommand<U, M>> resultCommands = new HashSet<>();
        for (Map.Entry<Trigger<U>, BaseCommand<U, M>> entry : commands.getEntrySet()) {
            if (entry.getKey().match(update)) resultCommands.add(entry.getValue());
        }

        return resultCommands;
    }

}
