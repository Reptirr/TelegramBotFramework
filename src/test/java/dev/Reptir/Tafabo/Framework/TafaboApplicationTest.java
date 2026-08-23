package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.Handlers.TafaboApplication;
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
        TafaboApplication<Update, Messenger> app = new TafaboApplication<>(new Messenger());
        try {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<Update> received = new AtomicReference<>();



            app.addCommand(
                    update -> update.value().equals("hello"),
                    ctx -> {
                        received.set(ctx.update());
                        latch.countDown();
                    }
            );

            Update update = new Update("hello");
            app.consumeUpdate(update);

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(update, received.get());
        } finally {
            app.cancelAllThreads();
        }
    }

    @Test
    void consumeUpdateDoesNotExecuteNonMatchingCommand() throws Exception {
        TafaboApplication<Update, Messenger> app = new TafaboApplication<>(new Messenger());

        try {
            CountDownLatch latch = new CountDownLatch(1);

            app.addCommand(
                    update -> false,
                    ctx -> latch.countDown()
            );


            app.consumeUpdate(new Update("hello"));

            assertFalse(latch.await(250, TimeUnit.MILLISECONDS));
        } finally {
            app.cancelAllThreads();
        }
    }

    @Test
    void contextContainsUpdateAndMessenger() throws Exception {
        Messenger messenger = new Messenger();
        TafaboApplication<Update, Messenger> app = new TafaboApplication<>(messenger);

        try {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicReference<Object> receivedMessenger = new AtomicReference<>();

            app.addCommand(
                    update -> true,
                    ctx -> {
                        receivedMessenger.set(ctx.messenger());
                        latch.countDown();
                    }
            );


            app.consumeUpdate(new Update("hello"));

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(messenger, receivedMessenger.get());
        } finally {
            app.cancelAllThreads();
        }
    }

    @Test
    void cancelAllMakeInterrupt() {
        TafaboApplication<Update, Messenger> app = new TafaboApplication<>(new Messenger());

        CountDownLatch interrupted = new CountDownLatch(3);

        try {
            app.addCommand(
                    e -> true,
                    ctx -> {
                        try {
                            while (true) {
                                Thread.sleep(10);
                            }
                        } catch (InterruptedException e) {
                            interrupted.countDown();
                            Thread.currentThread().interrupt();
                        }
                    }
            );

            for (int i = 0; i < 3; i++) { // create 3 threads
                app.consumeUpdate(new Update("123"));
            }

            Thread.sleep(100);

            app.cancelAllThreads();
            System.out.println("cancelled");

            assertTrue(interrupted.await(100, TimeUnit.MILLISECONDS));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
