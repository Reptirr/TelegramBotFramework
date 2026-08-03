package com.Reptir.Tafabo.Framework;

import com.Reptir.Tafabo.Framework.AdapterLogic.TafaboAdapter;
import com.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class TafaboAdapterTest {

    private record Update(String value) {
    }

    private static final class Messenger {
    }

    private static class TestAdapter extends TafaboAdapter<Update, Messenger> {

        private final AtomicInteger starts = new AtomicInteger();
        private final AtomicInteger stops = new AtomicInteger();

        TestAdapter() {
            super(new Messenger());
        }

        TestAdapter(Messenger messenger) {
            super(messenger);
        }

        @Override
        protected void onStart() {
            starts.incrementAndGet();
        }

        @Override
        protected void onStop() {
            stops.incrementAndGet();
        }

        void send(Update update) {
            onUpdate(update);
        }

        int starts() {
            return starts.get();
        }

        int stops() {
            return stops.get();
        }
    }

    @Test
    void initiallyNotRunning() {
        TestAdapter adapter = new TestAdapter();

        assertFalse(adapter.running());
    }

    @Test
    void startSetsRunningAndCallsOnStart() {
        TestAdapter adapter = new TestAdapter();

        adapter.start();

        try {
            assertTrue(adapter.running());
            assertEquals(1, adapter.starts());
        } finally {
            adapter.stop();
        }
    }

    @Test
    void repeatedStartDoesNotReinitializeAdapter() {
        TestAdapter adapter = new TestAdapter();

        adapter.start();
        adapter.start();

        try {
            assertTrue(adapter.running());
            assertEquals(1, adapter.starts());
        } finally {
            adapter.stop();
        }
    }

    @Test
    void stopSetsNotRunningAndCallsOnStop() {
        TestAdapter adapter = new TestAdapter();

        adapter.start();
        adapter.stop();

        assertFalse(adapter.running());
        assertEquals(1, adapter.stops());
    }

    @Test
    void repeatedStopDoesNothing() {
        TestAdapter adapter = new TestAdapter();

        adapter.start();
        adapter.stop();
        adapter.stop();

        assertEquals(1, adapter.stops());
        assertFalse(adapter.running());
    }

    @Test
    void stopBeforeStartDoesNothing() {
        TestAdapter adapter = new TestAdapter();

        adapter.stop();

        assertFalse(adapter.running());
        assertEquals(0, adapter.starts());
        assertEquals(0, adapter.stops());
    }

    @Test
    void startExceptionDoesNotEscapeAndAdapterRemainsStopped() {
        TafaboAdapter<Update, Messenger> adapter =
                new TafaboAdapter<>(new Messenger()) {

                    @Override
                    protected void onStart() {
                        throw new RuntimeException("boom");
                    }

                    @Override
                    protected void onStop() {
                    }
                };

        assertDoesNotThrow(adapter::start);
        assertFalse(adapter.running());
    }

    @Test
    void stopExceptionDoesNotEscapeAndAdapterStillStops() {
        TafaboAdapter<Update, Messenger> adapter =
                new TafaboAdapter<>(new Messenger()) {

                    @Override
                    protected void onStart() {
                    }

                    @Override
                    protected void onStop() {
                        throw new RuntimeException("boom");
                    }
                };

        adapter.start();

        assertDoesNotThrow(adapter::stop);
        assertFalse(adapter.running());
    }

    @Test
    void updateReachesRegisteredCommand() throws Exception {
        TestAdapter adapter = new TestAdapter();

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Update> received = new AtomicReference<>();

        Trigger<Update> trigger =
                update -> update.value().equals("target");

        adapter.addCommand(
                trigger,
                ctx -> {
                    received.set(ctx.update());
                    latch.countDown();
                }
        );

        adapter.start();

        try {
            Update update = new Update("target");

            adapter.send(update);

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(update, received.get());
        } finally {
            adapter.stop();
        }
    }

    @Test
    void nonMatchingUpdateDoesNotExecuteCommand() throws Exception {
        TestAdapter adapter = new TestAdapter();

        CountDownLatch latch = new CountDownLatch(1);

        adapter.addCommand(
                update -> update.value().equals("target"),
                ctx -> latch.countDown()
        );

        adapter.start();

        try {
            adapter.send(new Update("other"));

            assertFalse(latch.await(250, TimeUnit.MILLISECONDS));
        } finally {
            adapter.stop();
        }
    }

    @Test
    void registeredCommandReceivesMessenger() throws Exception {
        Messenger messenger = new Messenger();
        TestAdapter adapter = new TestAdapter(messenger);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Messenger> received = new AtomicReference<>();

        adapter.addCommand(
                update -> true,
                ctx -> {
                    received.set(ctx.messenger());
                    latch.countDown();
                }
        );

        adapter.start();

        try {
            adapter.send(new Update("x"));

            assertTrue(latch.await(2, TimeUnit.SECONDS));
            assertSame(messenger, received.get());
        } finally {
            adapter.stop();
        }
    }

    @Test
    void startStopCanBeRepeated() {
        TestAdapter adapter = new TestAdapter();

        for (int i = 0; i < 3; i++) {
            adapter.start();
            assertTrue(adapter.running());

            adapter.stop();
            assertFalse(adapter.running());
        }

        assertEquals(3, adapter.starts());
        assertEquals(3, adapter.stops());
    }
}
