package com.Reptir.Tafabo.Framework;

import com.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import com.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import com.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class RegistryCommandTest {

    private record Update(String value) {}
    private static final class Messenger {}

    @Test
    void registerAddsCommand() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        Trigger<Update> trigger = update -> true;
        BaseCommand<Update, Messenger> command = ctx -> {};

        registry.register(trigger, command);

        assertEquals(1, registry.getEntrySet().size());
        assertSame(command, registry.getEntrySet().iterator().next().getValue());
        assertSame(trigger, registry.getEntrySet().iterator().next().getKey());
    }

    @Test
    void registeringSameTriggerReplacesPreviousCommand() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        Trigger<Update> trigger = update -> true;
        BaseCommand<Update, Messenger> first = ctx -> {};
        BaseCommand<Update, Messenger> second = ctx -> {};

        registry.register(trigger, first);
        registry.register(trigger, second);

        assertEquals(1, registry.getEntrySet().size());
        assertSame(second, registry.getEntrySet().iterator().next().getValue());
    }

    @Test
    void registeringSameTriggerAndCommandDoesNotCreateDuplicate() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        Trigger<Update> trigger = update -> true;
        BaseCommand<Update, Messenger> command = ctx -> {};

        registry.register(trigger, command);
        registry.register(trigger, command);

        assertEquals(1, registry.getEntrySet().size());
    }

    @Test
    void multipleTriggersCanBeRegistered() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        Trigger<Update> first = update -> update.value().equals("a");
        Trigger<Update> second = update -> update.value().equals("b");

        registry.register(first, ctx -> {});
        registry.register(second, ctx -> {});

        assertEquals(2, registry.getEntrySet().size());
    }
}
