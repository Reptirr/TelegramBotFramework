package dev.Reptir.Tafabo.Framework.MiddlewareLogic;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryMiddleware;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;

import java.util.Set;

public abstract class MiddlewareRegistrator<U, M> {

    protected Middleware<MiddlewareArg<U, U, M>, PipelineState> onBeforeCommandsSearching() {
        return null;
    }

    protected Middleware<MiddlewareArg<Set<Trigger<U>>, U, M>, PipelineState> onAfterCommandsSearching() {
        return null;
    }

    protected Middleware<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState> onBeforeCommandsExecuting() {
        return null;
    }

    protected Middleware<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState> onBeforeCommandExecute() {
        return null;
    }

    protected Middleware<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState> onAfterCommandExecute() {
        return null;
    }

    protected Middleware<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState> onAfterCommandsExecuting() {
        return null;
    }

    protected Middleware<ExceptionMiddlewareArg<U, M>, PipelineState> onException() {
        return null;
    }

    public void init(RegistryMiddleware<U, M> storage) {
        var beforeCommandSearching = onBeforeCommandsSearching();
        if (beforeCommandSearching != null)
            storage.addBeforeCommandsSearching(beforeCommandSearching);

        var afterCommandSearching = onAfterCommandsSearching();
        if (afterCommandSearching != null)
            storage.addAfterCommandsSearching(afterCommandSearching);

        var beforeCommandsExecuting = onBeforeCommandsExecuting();
        if (beforeCommandsExecuting != null)
            storage.addBeforeCommandsExecuting(beforeCommandsExecuting);

        var beforeCommandExecute = onBeforeCommandExecute();
        if (beforeCommandExecute != null)
            storage.addBeforeCommandExecute(beforeCommandExecute);

        var afterCommandExecute = onAfterCommandExecute();
        if (afterCommandExecute != null)
            storage.addAfterCommandExecute(afterCommandExecute);

        var afterCommandsExecuting = onAfterCommandsExecuting();
        if (afterCommandsExecuting != null)
            storage.addAfterCommandsExecuting(afterCommandsExecuting);

        var exception = onException();
        if (exception != null)
            storage.addException(exception);
    }
}