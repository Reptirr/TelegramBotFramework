package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.AdapterLogic.TafaboAdapter;
import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.*;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class MiddlewareTest {
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

    @Test
    void MiddlewareRunningTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(6);
        AtomicBoolean beforeCommandSearching = new AtomicBoolean();
        AtomicBoolean afterCommandSearching = new AtomicBoolean();
        AtomicBoolean beforeCommandsExecuting = new AtomicBoolean();
        AtomicBoolean beforeCommandExecute = new AtomicBoolean();
        AtomicBoolean afterCommandExecute = new AtomicBoolean();
        AtomicBoolean afterCommandsExecuting = new AtomicBoolean();

        MiddlewareRegistrator<Object, Object> testMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Object, Object, Object>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                        0,
                        arg -> {
                            beforeCommandSearching.set(true);
                            latch.countDown();
                            return PipelineState.CONTINUE;
                        }
                );
            }

            @Override
            protected Middleware<MiddlewareArg<Set<Trigger<Object>>, Object, Object>, PipelineState> onAfterCommandsSearching() {
                return new Middleware<>(
                        0,
                        arg -> {
                            afterCommandSearching.set(true);
                            latch.countDown();
                            return PipelineState.CONTINUE;
                        }
                );
            }

            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Object, Object>>, Object, Object>, PipelineState> onBeforeCommandsExecuting() {
                return new Middleware<>(
                        0,
                        arg -> {
                            beforeCommandsExecuting.set(true);
                            latch.countDown();
                            return PipelineState.CONTINUE;
                        }
                );
            }

            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Object, Object>, Object, Object>, PipelineState> onBeforeCommandExecute() {
                return new Middleware<>(
                        0,
                        arg -> {
                            latch.countDown();
                            beforeCommandExecute.set(true);
                            return PipelineState.CONTINUE;
                        }
                );
            }

            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Object, Object>, Object, Object>, PipelineState> onAfterCommandExecute() {
                return new Middleware<>(
                        0,
                        arg -> {
                            afterCommandExecute.set(true);
                            latch.countDown();
                            return PipelineState.CONTINUE;
                        }
                );
            }

            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Object, Object>>, Object, Object>, PipelineState> onAfterCommandsExecuting() {
                return new Middleware<>(
                        0,
                        arg -> {
                            afterCommandsExecuting.set(true);
                            latch.countDown();
                            return PipelineState.CONTINUE;
                        }
                );
            }
        };

        class MyAdapter extends TafaboAdapter<Object, Object> {
            public MyAdapter(Object messenger) {
                super(messenger);
            }

            @Override
            protected void onStart() {

            }

            @Override
            protected void onStop() {

            }

            public void send(Object update) {
                onUpdate(update);
            }
        }

        MyAdapter adapter = new MyAdapter(new Object());

        adapter.addCommand(update -> true, ctx -> System.out.println("i execute")); // for before/afterCommandExecute middlewares

        adapter.addMiddleware(testMiddleware);

        adapter.start();

        adapter.send(new Object());


        assertTrue(latch.await(2, TimeUnit.SECONDS));

        assertTrue(beforeCommandSearching.get());
        assertTrue(afterCommandSearching.get());
        assertTrue(beforeCommandsExecuting.get());
        assertTrue(afterCommandsExecuting.get());
        assertTrue(afterCommandExecute.get());
        assertTrue(beforeCommandExecute.get());
    }

    @Test
    void MiddlewareRegisterBeforeStart() {
        AtomicBoolean work = new AtomicBoolean();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                    0,
                    arg -> {
                        work.set(true);
                        return PipelineState.CONTINUE;
                    }
                );
            }
        };

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);

        adapter.start();

        adapter.send(new Update(""));

        assertTrue(work.get());
    }

    @Test
    void MiddlewareRegisterAfterStart() {
        AtomicBoolean work = new AtomicBoolean();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                        0,
                        arg -> {
                            work.set(true);
                            return PipelineState.STOP;
                        }
                );
            }
        };

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.start();

        adapter.addMiddleware(middleware);

        adapter.send(new Update(""));

        assertTrue(work.get());
    }

    @Test
    void MiddlewarePriorityWork() throws InterruptedException {
        Queue<Integer> queue = new ConcurrentLinkedQueue<>();

        MiddlewareRegistrator<Update, Messenger> firstMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                        0,
                        arg -> {
                            queue.add(1);
                            return PipelineState.CONTINUE;
                        }
                );
            }
        };

        MiddlewareRegistrator<Update, Messenger> secondMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                        10,
                        arg -> {
                            queue.add(2);
                            return PipelineState.CONTINUE;
                        }

                );
            }
        };

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(secondMiddleware);
        adapter.addMiddleware(firstMiddleware);

        adapter.start();

        adapter.send(new Update(""));

        Thread.sleep(100);

        assertEquals(queue.poll(), Integer.valueOf(1));
        assertEquals(queue.poll(), Integer.valueOf(2));
    }

    @Test
    void MiddlewarePipelineStateStopping() {
        AtomicBoolean secondWork = new AtomicBoolean();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                        0,
                        arg -> PipelineState.STOP
                );
            }

            @Override
            protected Middleware<MiddlewareArg<Set<Trigger<Update>>, Update, Messenger>, PipelineState> onAfterCommandsSearching() {
                return new Middleware<>(
                        0,
                        arg -> {
                            secondWork.set(true);
                            return PipelineState.CONTINUE;
                        }
                );
            }
        };

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);

        adapter.start();

        adapter.send(new Update(""));

        assertFalse(secondWork.get());
    }

    // arguments

    @Test
    void MiddlewareBeforeCommandsSearchingArguments() {
        AtomicReference<String> arg = new AtomicReference<>("");

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(
                        0,
                        args -> {
                                 arg.set(args.arg().value());
                                 return PipelineState.STOP;
                        }
                );
            }
        };

        MyAdapter adapter = new MyAdapter(new Messenger());
        adapter.addMiddleware(middleware);
        adapter.start();

        adapter.send(new Update("test_string"));

        assertEquals("test_string", arg.get());
    }

    @Test
    void MiddlewareAfterCommandsSearchingArguments() {
        AtomicReference<Set<Trigger<Update>>> triggersFromMiddleware = new AtomicReference<>(new HashSet<>());

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<Set<Trigger<Update>>, Update, Messenger>, PipelineState> onAfterCommandsSearching() {
                return new Middleware<>(
                        0,
                        args -> {
                            triggersFromMiddleware.set(args.arg());
                            return PipelineState.STOP;
                        }
                );
            }
        };

        Set<Trigger<Update>> registeredTriggers = new HashSet<>();

        Trigger<Update> trigger1 = update -> true;
        Trigger<Update> trigger2 = update -> true;

        registeredTriggers.add(trigger1);
        registeredTriggers.add(trigger2);

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);

        adapter.addCommand(trigger1, ctx -> {});
        adapter.addCommand(trigger2, ctx -> {});

        adapter.start();

        adapter.send(new Update(""));

        assertEquals(registeredTriggers, triggersFromMiddleware.get());
    }

    @Test
    void MiddlewareBeforeCommandsExecutingArguments() {
        AtomicReference<Set<BaseCommand<Update, Messenger>>> commandsFromMiddleware = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Update, Messenger>>, Update, Messenger>, PipelineState> onBeforeCommandsExecuting() {
                return new Middleware<>(
                        0,
                        args -> {
                            commandsFromMiddleware.set(args.arg());
                            return PipelineState.STOP;
                        }
                );
            }
        };

        Set<BaseCommand<Update, Messenger>> registeredCommands = new HashSet<>();

        BaseCommand<Update, Messenger> command1 = ctx -> System.currentTimeMillis();
        BaseCommand<Update, Messenger> command2 = ctx -> {};

        registeredCommands.add(command1);
        registeredCommands.add(command2);

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);

        adapter.addCommand(update -> true, command1);
        adapter.addCommand(update -> true, command2);

        adapter.start();

        adapter.send(new Update(""));

        assertEquals(registeredCommands, commandsFromMiddleware.get());

    }

    @Test
    void MiddlewareBeforeCommandExecuteArguments() throws InterruptedException {
        AtomicReference<BaseCommand<Update, Messenger>> commandFromMiddleware = new AtomicReference<>();


        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Update, Messenger>, Update, Messenger>, PipelineState> onBeforeCommandExecute() {
                return new Middleware<>(
                        0,
                        args -> {
                            commandFromMiddleware.set(args.arg());
                            return PipelineState.STOP;
                        }
                );
            }
        };

        BaseCommand<Update, Messenger> command = ctx -> {};

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, command);

        adapter.start();
        adapter.send(new Update(""));

        Thread.sleep(10);

        assertEquals(command, commandFromMiddleware.get());
    }

    @Test
    void MiddlewareAfterCommandExecuteArguments() throws InterruptedException {
        AtomicReference<BaseCommand<Update, Messenger>> commandFromMiddleware = new AtomicReference<>();


        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<BaseCommand<Update, Messenger>, Update, Messenger>, PipelineState> onAfterCommandExecute() {
                return new Middleware<>(
                        0,
                        args -> {
                            commandFromMiddleware.set(args.arg());
                            return PipelineState.STOP;
                        }
                );
            }
        };

        BaseCommand<Update, Messenger> command = ctx -> {};

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, command);

        adapter.start();
        adapter.send(new Update(""));

        Thread.sleep(10);

        assertEquals(command, commandFromMiddleware.get());
    }

    @Test
    void MiddlewareAfterCommandsExecutingArguments() {
        AtomicReference<Set<BaseCommand<Update, Messenger>>> commandsFromMiddleware = new AtomicReference<>();

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<Update, Messenger>() {
            @Override
            protected Middleware<MiddlewareArg<Set<BaseCommand<Update, Messenger>>, Update, Messenger>, PipelineState> onAfterCommandsExecuting() {
                return new Middleware<>(
                        0,
                        args -> {
                            commandsFromMiddleware.set(args.arg());
                            return PipelineState.STOP;
                        }
                );
            }
        };

        Set<BaseCommand<Update, Messenger>> registeredCommands = new HashSet<>();

        BaseCommand<Update, Messenger> command1 = ctx -> System.currentTimeMillis();
        BaseCommand<Update, Messenger> command2 = ctx -> {};

        registeredCommands.add(command1);
        registeredCommands.add(command2);

        MyAdapter adapter = new MyAdapter(new Messenger());

        adapter.addMiddleware(middleware);

        adapter.addCommand(update -> true, command1);
        adapter.addCommand(update -> true, command2);

        adapter.start();

        adapter.send(new Update(""));

        assertEquals(registeredCommands, commandsFromMiddleware.get());

    }

    // === Multiple middlewares interaction ===

    @Test
    void MultipleMiddlewares_ExecuteInPriorityOrder() throws InterruptedException {
        Queue<Integer> executionOrder = new ConcurrentLinkedQueue<>();
        CountDownLatch latch = new CountDownLatch(3);

        MiddlewareRegistrator<Update, Messenger> lowPriority = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(100, args -> {
                    executionOrder.add(3);
                    latch.countDown();
                    return PipelineState.CONTINUE;
                });
            }
        };

        MiddlewareRegistrator<Update, Messenger> mediumPriority = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(50, args -> {
                    executionOrder.add(2);
                    latch.countDown();
                    return PipelineState.CONTINUE;
                });
            }
        };

        MiddlewareRegistrator<Update, Messenger> highPriority = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    executionOrder.add(1);
                    latch.countDown();
                    return PipelineState.CONTINUE;
                });
            }
        };

        adapter.addMiddleware(lowPriority);
        adapter.addMiddleware(mediumPriority);
        adapter.addMiddleware(highPriority);

        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(Integer.valueOf(1), executionOrder.poll());
        assertEquals(Integer.valueOf(2), executionOrder.poll());
        assertEquals(Integer.valueOf(3), executionOrder.poll());
    }

    @Test
    void Middleware_CanModifyPipelineData() throws InterruptedException {
        AtomicReference<String> capturedValue = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    // Middleware can see the update before commands are searched
                    capturedValue.set(args.arg().value());
                    latch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(middleware);
        adapter.start();
        adapter.send(new Update("modified_test"));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals("modified_test", capturedValue.get());
    }

    @Test
    void Middleware_StopImmediately_PreventsCommandExecution() throws InterruptedException {
        CountDownLatch commandLatch = new CountDownLatch(1);
        AtomicBoolean commandExecuted = new AtomicBoolean(false);

        MiddlewareRegistrator<Update, Messenger> middleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> PipelineState.STOP);
            }
        };

        adapter.addMiddleware(middleware);
        adapter.addCommand(update -> true, ctx -> {
            commandExecuted.set(true);
            commandLatch.countDown();
        });

        adapter.start();
        adapter.send(new Update("test"));

        // Command should not execute due to STOP
        assertFalse(commandLatch.await(200, TimeUnit.MILLISECONDS));
        assertFalse(commandExecuted.get());
    }

    @Test
    void MultipleStopsInPipeline_OnlyFirstStops() throws InterruptedException {
        Queue<Integer> executionOrder = new ConcurrentLinkedQueue<>();
        CountDownLatch latch = new CountDownLatch(1);

        MiddlewareRegistrator<Update, Messenger> firstMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(0, args -> {
                    executionOrder.add(1);
                    latch.countDown();
                    return PipelineState.STOP;
                });
            }
        };

        MiddlewareRegistrator<Update, Messenger> secondMiddleware = new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, Messenger>, PipelineState> onBeforeCommandsSearching() {
                return new Middleware<>(10, args -> {
                    executionOrder.add(2);
                    return PipelineState.STOP;
                });
            }
        };

        adapter.addMiddleware(secondMiddleware);
        adapter.addMiddleware(firstMiddleware);

        adapter.start();
        adapter.send(new Update("test"));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(Integer.valueOf(1), executionOrder.poll());
        assertNull(executionOrder.poll()); // Second middleware should not execute
    }
}
