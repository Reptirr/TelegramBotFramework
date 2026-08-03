package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import dev.Reptir.Tafabo.Framework.ThreadLogic.ThreadId;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class RegistryThreadTest {

    @Test
    void createThreadReturnsIdAndTracksRunningTask() throws Exception {
        RegistryThread registry = new RegistryThread();
        try {
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch release = new CountDownLatch(1);

            ThreadId id = registry.createThread(() -> {
                started.countDown();
                try {
                    release.await();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            });

            assertNotNull(id);
            assertTrue(started.await(2, TimeUnit.SECONDS));
            assertTrue(registry.getThreads().containsKey(id));

            release.countDown();
        } finally {
            registry.shutdown();
        }
    }

    @Test
    void completedTaskIsRemovedFromRegistry() throws Exception {
        RegistryThread registry = new RegistryThread();
        try {
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch release = new CountDownLatch(1);

            ThreadId id = registry.createThread(() -> {
                started.countDown();
                try {
                    release.await();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            });

            assertTrue(started.await(2, TimeUnit.SECONDS));
            assertTrue(registry.getThreads().containsKey(id));

            release.countDown();

            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(2);
            while (registry.getThreads().containsKey(id)
                    && System.nanoTime() < deadline) {
                Thread.yield();
            }

            assertFalse(registry.getThreads().containsKey(id));
        } finally {
            registry.shutdown();
        }
    }

    @Test
    void cancelThreadInterruptsRunningTask() throws Exception {
        RegistryThread registry = new RegistryThread();
        try {
            CountDownLatch started = new CountDownLatch(1);
            CountDownLatch interrupted = new CountDownLatch(1);

            ThreadId id = registry.createThread(() -> {
                started.countDown();
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                } catch (InterruptedException e) {
                    interrupted.countDown();
                    Thread.currentThread().interrupt();
                }
            });

            assertTrue(started.await(2, TimeUnit.SECONDS));

            registry.cancelThread(id);

            assertTrue(interrupted.await(2, TimeUnit.SECONDS));
        } finally {
            registry.shutdown();
        }
    }

    @Test
    void cancelUnknownThreadDoesNothing() {
        RegistryThread registry = new RegistryThread();
        try {
            assertDoesNotThrow(() -> registry.cancelThread(new ThreadId()));
        } finally {
            registry.shutdown();
        }
    }

    @Test
    void shutdownIsIdempotent() {
        RegistryThread registry = new RegistryThread();

        assertDoesNotThrow(() -> {
            registry.shutdown();
            registry.shutdown();
        });
    }

    @Test
    void taskCanActuallyRun() throws Exception {
        RegistryThread registry = new RegistryThread();
        try {
            CountDownLatch latch = new CountDownLatch(1);
            AtomicBoolean executed = new AtomicBoolean();

            registry.createThread(() -> {
                executed.set(true);
                latch.countDown();
            });

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertTrue(executed.get());
        } finally {
            registry.shutdown();
        }
    }
}
