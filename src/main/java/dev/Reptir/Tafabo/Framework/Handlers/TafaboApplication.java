package dev.Reptir.Tafabo.Framework.Handlers;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.CommandLogic.CommandRouter;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareArg;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareManager;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareRegistrator;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.PipelineState;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import dev.Reptir.Tafabo.Framework.dto.Context;
import kotlin.Pair;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class TafaboApplication<U, M> {
    private final RegistryThread threadRegistry;
    private final CommandRouter<U, M> router;
    private final MiddlewareManager<U, M> middlewareManager = new MiddlewareManager<>();

    M messenger;

    public TafaboApplication(RegistryCommand<U, M> commandRegistry, M messenger, RegistryThread registryThread) {
        this.router = new CommandRouter<>(commandRegistry);
        this.messenger = messenger;
        this.threadRegistry = registryThread;
    }

    public void consumeUpdate(U update) {
        Context<U, M> ctx = new Context<>(update, messenger);

        if (middlewareManager.runBeforeCommandsSearching(new MiddlewareArg<>(update, ctx)) == PipelineState.STOP) return; // middleware

        Pair<Set<Trigger<U>>, Set<BaseCommand<U, M>>> matches = router.getMatched(update); // logic

        if (middlewareManager.runAfterCommandsSearching(new MiddlewareArg<>(matches.component1(), ctx)) == PipelineState.STOP) return; // middleware


        if (middlewareManager.runBeforeCommandsExecuting(new MiddlewareArg<>(matches.component2(), ctx)) == PipelineState.STOP) return; // middleware

        AtomicBoolean stopped = new AtomicBoolean(false);

        for (var command : matches.component2()) {
            threadRegistry.createThread(() -> {
                if (middlewareManager.runBeforeCommandExecute(new MiddlewareArg<>(command, ctx)) == PipelineState.STOP) return; // middleware
                command.execute(ctx);                                                                                           // logic
                if (middlewareManager.runAfterCommandExecute(new MiddlewareArg<>(command, ctx)) == PipelineState.STOP) stopped.set(true); // middleware
            });
        }

        if (!stopped.get())
            middlewareManager.runAfterCommandsExecuting(new MiddlewareArg<>(matches.component2(), ctx)); // middleware. probably remove in future versions
    }

    public void addMiddleware(MiddlewareRegistrator<U, M> middlewareRegistrator) {
        middlewareManager.registerMiddleware(middlewareRegistrator);
    }
}
