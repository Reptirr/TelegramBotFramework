package com.Reptir.Tafabo.Framework.Registries;

import com.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import com.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class RegistryCommand<U, M> {
    private final Map<Trigger<U>, BaseCommand<U, M>> commands = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(RegistryCommand.class);

    public void register(Trigger<U> trigger, BaseCommand<U, M> entry) {
        if (Objects.equals(commands.get(trigger), entry)) {
            logger.warn("Entry trigger - command already exists. Skipping");
            return;
        }

        commands.put(trigger, entry);
    }

    public Set<Map.Entry<Trigger<U>, BaseCommand<U, M>>> getEntrySet() {
        return commands.entrySet();
    }

}