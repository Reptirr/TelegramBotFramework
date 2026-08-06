package dev.Reptir.Tafabo.Framework.CommandLogic;

import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import kotlin.Pair;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommandRouter<U, M> {
    private final RegistryCommand<U, M> commands;

    public CommandRouter(RegistryCommand<U, M> commands) {
        this.commands = commands;
    }


    public Pair<Set<Trigger<U>>, Set<BaseCommand<U, M>>> getMatched(U update) {
        Set<BaseCommand<U, M>> resultCommands = new HashSet<>();
        Set<Trigger<U>> matchedTriggers = new HashSet<>();

        for (Map.Entry<Trigger<U>, BaseCommand<U, M>> entry : commands.getEntrySet()) {
            if (entry.getKey().match(update)) {
                resultCommands.add(entry.getValue());
                matchedTriggers.add(entry.getKey());
            }
        }

        return new Pair<>(matchedTriggers, resultCommands);
    }

}
