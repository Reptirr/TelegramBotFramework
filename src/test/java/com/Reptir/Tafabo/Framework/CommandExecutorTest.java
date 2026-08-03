package com.Reptir.Tafabo.Framework;

import com.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import com.Reptir.Tafabo.Framework.CommandLogic.CommandExecutor;
import com.Reptir.Tafabo.Framework.Registries.RegistryThread;
import com.Reptir.Tafabo.Framework.dto.Context;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class CommandExecutorTest {

    private record Update(String value) {}
    private static final class Messenger {}

    @Test
    void executesEveryCommand() throws Exception {
        RegistryThread threads = new RegistryThread();
        try {
            CountDownLatch latch = new CountDownLatch(2);
            AtomicInteger count = new AtomicInteger();

            BaseCommand<Update, Messenger> first = ctx -> {
                count.incrementAndGet();
                latch.countDown();
            };
            BaseCommand<Update, Messenger> second = ctx -> {
                count.incrementAndGet();
                latch.countDown();
            };

            new CommandExecutor<Update, Messenger>(threads).executeAll(
                    Set.of(first, second),
                    new Context<>(new Update("x"), new Messenger())
            );

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertEquals(2, count.get());
        } finally {
            threads.shutdown();
        }
    }

    @Test
    void passesSameContextToCommand() throws Exception {
        RegistryThread threads = new RegistryThread();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Context<Update, Messenger> expected =
                    new Context<>(new Update("x"), new Messenger());
            Context<?, ?>[] actual = new Context<?, ?>[1];

            new CommandExecutor<Update, Messenger>(threads).executeAll(
                    Set.of(ctx -> {
                        actual[0] = ctx;
                        latch.countDown();
                    }),
                    expected
            );

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(expected, actual[0]);
        } finally {
            threads.shutdown();
        }
    }

    @Test
    void nullContextIsIgnored() {
        RegistryThread threads = new RegistryThread();
        try {
            assertDoesNotThrow(() ->
                    new CommandExecutor<Update, Messenger>(threads)
                            .executeAll(Set.of(ctx -> fail("must not execute")), null)
            );
            assertTrue(threads.getThreads().isEmpty());
        } finally {
            threads.shutdown();
        }
    }
}
