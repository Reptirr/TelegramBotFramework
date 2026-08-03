package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.Handlers.TafaboApplication;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TafaboApplicationTest {

    private record Update(String value) {}
    private static final class Messenger {}

    @Test
    void consumeUpdateExecutesMatchingCommand() throws Exception {
        RegistryCommand<Update, Messenger> commands = new RegistryCommand<>();
        RegistryThread threads = new RegistryThread();

        try {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<Update> received = new AtomicReference<>();

            commands.register(
                    update -> update.value().equals("hello"),
                    ctx -> {
                        received.set(ctx.update());
                        latch.countDown();
                    }
            );

            TafaboApplication<Update, Messenger> app =
                    new TafaboApplication<>(
                            commands,
                            new Messenger(),
                            new CommandExecutor<>(threads),
                            threads
                    );

            Update update = new Update("hello");
            app.consumeUpdate(update);

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(update, received.get());
        } finally {
            threads.shutdown();
        }
    }

    @Test
    void consumeUpdateDoesNotExecuteNonMatchingCommand() throws Exception {
        RegistryCommand<Update, Messenger> commands = new RegistryCommand<>();
        RegistryThread threads = new RegistryThread();

        try {
            CountDownLatch latch = new CountDownLatch(1);

            commands.register(
                    update -> false,
                    ctx -> latch.countDown()
            );

            TafaboApplication<Update, Messenger> app =
                    new TafaboApplication<>(
                            commands,
                            new Messenger(),
                            new CommandExecutor<>(threads),
                            threads
                    );

            app.consumeUpdate(new Update("hello"));

            assertFalse(latch.await(250, TimeUnit.MILLISECONDS));
        } finally {
            threads.shutdown();
        }
    }

    @Test
    void contextContainsUpdateAndMessenger() throws Exception {
        RegistryCommand<Update, Messenger> commands = new RegistryCommand<>();
        RegistryThread threads = new RegistryThread();
        Messenger messenger = new Messenger();

        try {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<Object> receivedMessenger = new AtomicReference<>();

            commands.register(
                    update -> true,
                    ctx -> {
                        receivedMessenger.set(ctx.messenger());
                        latch.countDown();
                    }
            );

            TafaboApplication<Update, Messenger> app =
                    new TafaboApplication<>(
                            commands,
                            messenger,
                            new CommandExecutor<>(threads),
                            threads
                    );

            app.consumeUpdate(new Update("hello"));

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(messenger, receivedMessenger.get());
        } finally {
            threads.shutdown();
        }
    }
}
