package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.AdapterLogic.TafaboAdapter;
import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.*;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MiddlewareExceptionTest {
    private record Update(String value) {
    }

    private static final class Messenger {
    }

    private class MyAdapter extends TafaboAdapter<Update, Messenger> {
        public MyAdapter(Messenger messenger) {
            super(messenger);
        }

        @Override
        protected void onStart() {
        }

        @Override
        protected void onStop() {
        }

        public void send(Update update) {
            onUpdate(update);
        }
    }

    private MyAdapter adapter;

    @BeforeEach
    void setup() {
        adapter = new MyAdapter(new Messenger());
    }

    // === Exception handling tests ===

    @Test
    void ExceptionInBeforeCommandsSearching_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();
        AtomicReference<Throwable> capturedException = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception in beforeCommandsSearching");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    capturedException.set(args.exception());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.BEFORE_COMMANDS_SEARCHING, capturedStage.get());
        assertTrue(capturedException.get() instanceof RuntimeException);
        assertEquals("Test exception in beforeCommandsSearching", capturedException.get().getMessage());
    }

    @Test
    void ExceptionInAfterCommandsSearching_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Set<Trigger<Update>>, Update, Messenger>, PipelineState> onAfterCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception in afterCommandsSearching");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.AFTER_COMMANDS_SEARCHING, capturedStage.get());
    }

    @Test
    void ExceptionInBeforeCommandsExecuting_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Update, Messenger>>, Update, Messenger>, PipelineState> onBeforeCommandsExecuting() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception in beforeCommandsExecuting");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.BEFORE_COMMANDS_EXECUTING, capturedStage.get());
    }

    @Test
    void ExceptionInBeforeCommandExecute_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Update, Messenger>, Update, Messenger>, PipelineState> onBeforeCommandExecute() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception in beforeCommandExecute");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.BEFORE_COMMAND_EXECUTE, capturedStage.get());
    }

    @Test
    void ExceptionInCommandExecute_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();
        AtomicReference<Throwable> capturedException = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    capturedException.set(args.exception());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {
            throw new RuntimeException("Command failed!");
        });
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.COMMAND_EXECUTE, capturedStage.get());
        assertTrue(capturedException.get() instanceof RuntimeException);
        assertEquals("Command failed!", capturedException.get().getMessage());
    }

    @Test
    void ExceptionInAfterCommandExecute_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Update, Messenger>, Update, Messenger>, PipelineState> onAfterCommandExecute() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception in afterCommandExecute");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.AFTER_COMMAND_EXECUTE, capturedStage.get());
    }

    @Test
    void ExceptionInAfterCommandsExecuting_CallsOnExceptionMiddleware() throws InterruptedException {
        CountDownLatch exceptionLatch = new CountDownLatch(1);
        AtomicReference<PipelineStage> capturedStage = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Update, Messenger>>, Update, Messenger>, PipelineState> onAfterCommandsExecuting() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception in afterCommandsExecuting");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedStage.set(args.stage());
                    exceptionLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionLatch.await(2, TimeUnit.SECONDS));
        assertEquals(PipelineStage.AFTER_COMMANDS_EXECUTING, capturedStage.get());
    }

    // === Exception middleware returning CONTINUE ===

    @Test
    void ExceptionMiddlewareContinue_DoesNotStopPipeline() throws InterruptedException {
        CountDownLatch afterCommandsExecutingLatch = new CountDownLatch(1);
        AtomicBoolean exceptionHandled = new AtomicBoolean(false);

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test exception");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    exceptionHandled.set(true);
                    return PipelineState.CONTINUE;
                });
            }

            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Update, Messenger>>, Update, Messenger>, PipelineState> onAfterCommandsExecuting() {
                return new Middleware<>(0, args -> {
                    afterCommandsExecutingLatch.countDown();
                    return PipelineState.CONTINUE;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(exceptionHandled.get());
        // After CONTINUE, pipeline should continue and reach afterCommandsExecuting
        assertTrue(afterCommandsExecutingLatch.await(2, TimeUnit.SECONDS));
    }

    // === Exception context tests ===

    @Test
    void ExceptionMiddleware_ReceivesCorrectContext() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Update> capturedUpdate = new AtomicReference<>();
        AtomicReference<Messenger> capturedMessenger = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedUpdate.set(args.ctx().update());
                    capturedMessenger.set(args.ctx().messenger());
                    latch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        Messenger testMessenger = new Messenger();
        MyAdapter adapter = new MyAdapter(testMessenger);
        Update testUpdate = new Update("context_test");

        adapter.addMiddleware(middleware);
        adapter.start();
        adapter.send(testUpdate);

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(testUpdate, capturedUpdate.get());
        assertEquals(testMessenger, capturedMessenger.get());
    }

    // === Multiple exception middlewares ===

    @Test
    void MultipleExceptionMiddlewares_ExecuteInPriorityOrder() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<Integer> executionOrder = new AtomicReference<>(0);

        MiddlewareRegistrator<Update, Messenger> firstMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    executionOrder.compareAndSet(0, 1);
                    latch.countDown();
                    return PipelineState.CONTINUE;
                });
            }
        };

        MiddlewareRegistrator<Update, Messenger> secondMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(10, args -> {
                    executionOrder.compareAndSet(1, 2);
                    latch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(secondMiddleware);
        adapter.addMiddleware(firstMiddleware);
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(Integer.valueOf(2), executionOrder.get());
    }

    @Test
    void ExceptionMiddlewareStop_StopsExceptionPipeline() throws InterruptedException {
        CountDownLatch firstLatch = new CountDownLatch(1);
        AtomicBoolean secondMiddlewareCalled = new AtomicBoolean(false);

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Test");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    firstLatch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        MiddlewareRegistrator<Update, Messenger> secondMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(10, args -> {
                    secondMiddlewareCalled.set(true);
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addMiddleware(secondMiddleware);
        adapter.start();
        adapter.send(new Update("test"));

        // First middleware counts down, but with STOP the second shouldn't be called
        assertTrue(firstLatch.await(2, TimeUnit.SECONDS));
        Thread.sleep(100); // Give time for potential second middleware call
        assertFalse(secondMiddlewareCalled.get());
    }

    // === No exception middleware registered ===

    @Test
    void NoExceptionMiddleware_ExceptionPropagates() {
        AtomicBoolean exceptionReachedHandler = new AtomicBoolean(false);

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    throw new RuntimeException("Uncaught exception");
                });
            }
            // No onException() override - returns null by default
        };

        adapter.addMiddleware(middleware);
        adapter.start();
        adapter.send(new Update("test"));

        // Without exception middleware, exception should propagate
        // The application should not crash (currently using @SneakyThrows)
        // This test documents current behavior
    }

    // === Different exception types ===

    @Test
    void ExceptionMiddleware_ReceivesDifferentExceptionTypes() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> capturedException = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Update, Messenger>, Update, Messenger>, PipelineState> onBeforeCommandExecute() {
                return new Middleware<>(0, args -> {
                    throw new IllegalArgumentException("Illegal argument");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedException.set(args.exception());
                    latch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertTrue(capturedException.get() instanceof IllegalArgumentException);
    }

    @Test
    void ExceptionMiddleware_ReceivesNullPointerException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> capturedException = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Update, Messenger>, Update, Messenger>, PipelineState> onBeforeCommandExecute() {
                return new Middleware<>(0, args -> {
                    throw new NullPointerException("NPE test");
                });
            }

            @Override
            protected Middleware<ExceptionMiddlewareArg<Update, Messenger>, PipelineState> onException() {
                return new Middleware<>(0, args -> {
                    capturedException.set(args.exception());
                    latch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {});
        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertTrue(capturedException.get() instanceof NullPointerException);
    }
}
