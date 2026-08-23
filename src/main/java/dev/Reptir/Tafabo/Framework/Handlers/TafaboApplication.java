package dev.Reptir.Tafabo.Framework.Handlers;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.CommandLogic.CommandRouter;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.*;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import dev.Reptir.Tafabo.Framework.dto.Context;
import kotlin.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TafaboApplication<U, M> {
    private final RegistryThread threadRegistry = new RegistryThread();
    private final MiddlewareManager<U, M> middlewareManager = new MiddlewareManager<>();

    private final RegistryCommand<U, M> commandRegistry = new RegistryCommand<>();
    private final CommandRouter<U, M> router = new CommandRouter<>(commandRegistry);

    M messenger;

    public TafaboApplication(M messenger) {
        this.messenger = messenger;
    }

    // cancel all threads. does not execute the remaining middlewares
    public void cancelAllThreads() {
        threadRegistry.cancelAllThreads();
    }

    public void addCommand(Trigger<U> trigger, BaseCommand<U, M> command) {
        commandRegistry.register(trigger, command);
    }
    public void setCommands(Map<Trigger<U>, BaseCommand<U, M>> commands) {
        commandRegistry.setMap(commands);
    }

    public void addMiddleware(MiddlewareRegistrator<U, M> middlewareRegistrator) {
        middlewareManager.registerMiddleware(middlewareRegistrator);
    }


    // handling update is sync.
    public void consumeUpdate(U update) {
        threadRegistry.createThread(() -> {

        Context<U, M> ctx = new Context<>(update, messenger);
        try {
            if (middlewareManager.runBeforeCommandsSearching(new MiddlewareArg<>(update, ctx)) == PipelineState.STOP)
                return; // middleware
        } catch (Exception e) {
            if (middlewareManager.runException(new ExceptionMiddlewareArg<>(e, PipelineStage.BEFORE_COMMANDS_SEARCHING, ctx)) == PipelineState.STOP) return;
        }

        Pair<Set<Trigger<U>>, Set<BaseCommand<U, M>>> matches = router.getMatched(update); // logic

        try {
            if (middlewareManager.runAfterCommandsSearching(new MiddlewareArg<>(matches.component1(), ctx)) == PipelineState.STOP)
                return; // middleware
        } catch (Exception e) {
            if (middlewareManager.runException(new ExceptionMiddlewareArg<>(e, PipelineStage.AFTER_COMMANDS_SEARCHING, ctx)) == PipelineState.STOP) return;
        }

        try {
            if (middlewareManager.runBeforeCommandsExecuting(new MiddlewareArg<>(matches.component2(), ctx)) == PipelineState.STOP)
                return; // middleware
        } catch (Exception e) {
            if (middlewareManager.runException(new ExceptionMiddlewareArg<>(e, PipelineStage.BEFORE_COMMANDS_EXECUTING, ctx)) == PipelineState.STOP) return;
        }

        AtomicBoolean stopped = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(matches.component2().size());
        for (var command : matches.component2()) {
            threadRegistry.createThread(() -> {
                try {
                    if (middlewareManager.runBeforeCommandExecute(new MiddlewareArg<>(command, ctx)) == PipelineState.STOP) {
                        stopped.set(true); // middleware
                        latch.countDown();
                        return;
                    }
                } catch (Exception e) {
                    if (middlewareManager.runException(
                            new ExceptionMiddlewareArg<>(e, PipelineStage.BEFORE_COMMAND_EXECUTE, ctx)
                    ) == PipelineState.STOP) {
                        stopped.set(true);
                        latch.countDown();
                        return;
                    }
                }

                try {
                    command.execute(ctx); // logic
                } catch (Exception e) {
                    if (middlewareManager.runException(
                            new ExceptionMiddlewareArg<>(e, PipelineStage.COMMAND_EXECUTE, ctx)
                    ) == PipelineState.STOP) {
                        stopped.set(true);
                        latch.countDown();
                        return;
                    }
                }

                try {
                    if (middlewareManager.runAfterCommandExecute(new MiddlewareArg<>(command, ctx)) == PipelineState.STOP) {
                        stopped.set(true); // middleware
                        latch.countDown();
                        return;
                    }
                } catch (Exception e) {
                    if (middlewareManager.runException(
                            new ExceptionMiddlewareArg<>(e, PipelineStage.AFTER_COMMAND_EXECUTE, ctx)
                    ) == PipelineState.STOP) {
                        stopped.set(true);
                        latch.countDown();
                        return;
                    }
                }

                latch.countDown();
            });
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Pipeline was interrupted on before middleware AfterCommandsExecuting");
            return;
        }


        if (stopped.get()) return;

        try {
            middlewareManager.runAfterCommandsExecuting(new MiddlewareArg<>(matches.component2(), ctx));
        } catch (Exception e) {
            middlewareManager.runException(new ExceptionMiddlewareArg<>(e, PipelineStage.AFTER_COMMANDS_EXECUTING, ctx));
        }

        });
    }
}
