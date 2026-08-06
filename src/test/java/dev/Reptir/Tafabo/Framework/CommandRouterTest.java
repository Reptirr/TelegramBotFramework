package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.CommandLogic.CommandRouter;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CommandRouterTest {

    private record Update(String value) {}
    private static final class Messenger {}

    @Test
    void returnsOnlyCommandsWhoseTriggersMatch() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        BaseCommand<Update, Messenger> matching = ctx -> {};
        BaseCommand<Update, Messenger> nonMatching = ctx -> {};

        registry.register(update -> update.value().equals("ok"), matching);
        registry.register(update -> update.value().equals("no"), nonMatching);

        Set<BaseCommand<Update, Messenger>> result =
                new CommandRouter<>(registry).getMatched(new Update("ok")).component2();

        assertEquals(1, result.size());
        assertTrue(result.contains(matching));
        assertFalse(result.contains(nonMatching));
    }

    @Test
    void returnsAllCommandsWhenMultipleTriggersMatch() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        BaseCommand<Update, Messenger> first = ctx -> {};
        BaseCommand<Update, Messenger> second = ctx -> {};

        registry.register(update -> true, first);
        registry.register(update -> true, second);

        Set<BaseCommand<Update, Messenger>> result =
                new CommandRouter<>(registry).getMatched(new Update("value")).component2();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Set.of(first, second)));
    }

    @Test
    void returnsEmptySetWhenNothingMatches() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        registry.register(update -> false, ctx -> {});

        assertTrue(new CommandRouter<>(registry)
                .getMatched(new Update("value")).component1()
                .isEmpty());
    }

    @Test
    void evaluatesTriggersAgainstSameUpdate() {
        RegistryCommand<Update, Messenger> registry = new RegistryCommand<>();
        Update expected = new Update("value");
        Update[] actual = new Update[1];

        Trigger<Update> trigger = update -> {
            actual[0] = update;
            return true;
        };

        registry.register(trigger, ctx -> {});

        new CommandRouter<>(registry).getMatched(expected);

        assertSame(expected, actual[0]);
    }
}
