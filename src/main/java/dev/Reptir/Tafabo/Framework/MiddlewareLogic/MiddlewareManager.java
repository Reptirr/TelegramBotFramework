package dev.Reptir.Tafabo.Framework.MiddlewareLogic;

/*
Middleware model:

1x BeforeCommandSearching(Update)
1x AfterCommandSearching(Set<Trigger> matches)


1x BeforeCommandsExecuting(Set<Command>)

Nx BeforeCommandExecute(Command)
Nx AfterCommandExecute(Command)

1x AfterCommandsExecuting(Set<Command>)


*/

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryMiddleware;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;

import java.util.List;
import java.util.Set;

public class MiddlewareManager<U, M> {
    private final RegistryMiddleware<U, M> middlewareRegistry = new RegistryMiddleware<>();

    public void registerMiddleware(MiddlewareRegistrator<U, M> registrator) {
        registrator.init(middlewareRegistry);
    }

    private <T> PipelineState run(
            List<Middleware<MiddlewareArg<T, U, M>, PipelineState>> middlewares,
            MiddlewareArg<T, U, M> value
    ) {
        for (var middleware : middlewares) {
            if (middleware.middleware().apply(value) == PipelineState.STOP)
                return PipelineState.STOP;
        }

        return PipelineState.CONTINUE;
    }

    public PipelineState runBeforeCommandsSearching(MiddlewareArg<U, U, M> arg) {
        return run(middlewareRegistry.getBeforeCommandsSearching(), arg);
    }
    public PipelineState runAfterCommandsSearching(MiddlewareArg<Set<Trigger<U>>, U, M> arg) {
        return run(middlewareRegistry.getAfterCommandsSearching(), arg);
    }
    public PipelineState runBeforeCommandsExecuting(MiddlewareArg<Set<BaseCommand<U, M>>, U, M> arg) {
        return run(middlewareRegistry.getBeforeCommandsExecuting(), arg);
    }
    public PipelineState runBeforeCommandExecute(MiddlewareArg<BaseCommand<U, M>, U, M> arg) {
        return run(middlewareRegistry.getBeforeCommandExecute(), arg);
    }
    public PipelineState runAfterCommandExecute(MiddlewareArg<BaseCommand<U, M>, U, M> arg) {
        return run(middlewareRegistry.getAfterCommandExecute(), arg);
    }
    public PipelineState runAfterCommandsExecuting(MiddlewareArg<Set<BaseCommand<U, M>>, U, M> arg) {
        return run(middlewareRegistry.getAfterCommandsExecuting(), arg);
    }

}
