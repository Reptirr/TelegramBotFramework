package dev.Reptir.Tafabo.Framework.Registries;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.Middleware;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareArg;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareList;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.PipelineState;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/*
Middleware model:

1x BeforeCommandSearching(Update, ctx)
1x AfterCommandSearching(Set<Trigger> matches, ctx)


1x BeforeCommandsExecuting(Set<Command>, ctx)

Nx BeforeCommandExecute(Command, ctx)
Nx AfterCommandExecute(Command, ctx)

1x AfterCommandsExecuting(Set<Command>, ctx)


*/

public class RegistryMiddleware<U, M> {
    private final MiddlewareList<MiddlewareArg<U, U, M>, PipelineState> beforeCommandsSearching = new MiddlewareList<>();
    private final MiddlewareList<MiddlewareArg<Set<Trigger<U>>, U, M>, PipelineState> afterCommandsSearching = new MiddlewareList<>();
    private final MiddlewareList<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState> beforeCommandsExecuting = new MiddlewareList<>();
    private final MiddlewareList<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState> beforeCommandExecute = new MiddlewareList<>();
    private final MiddlewareList<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState> afterCommandExecute = new MiddlewareList<>();
    private final MiddlewareList<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState> afterCommandsExecuting = new MiddlewareList<>();

    public void addBeforeCommandsSearching(Middleware<MiddlewareArg<U, U, M>, PipelineState> middleware) {
        beforeCommandsSearching.add(middleware);
    }
    public void addAfterCommandsSearching(Middleware<MiddlewareArg<Set<Trigger<U>>, U, M>, PipelineState> middleware) {
        afterCommandsSearching.add(middleware);
    }
    public void addBeforeCommandsExecuting(Middleware<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState> middleware) {
        beforeCommandsExecuting.add(middleware);
    }
    public void addBeforeCommandExecute(Middleware<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState> middleware) {
        beforeCommandExecute.add(middleware);
    }
    public void addAfterCommandExecute(Middleware<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState> middleware) {
        afterCommandExecute.add(middleware);
    }
    public void addAfterCommandsExecuting(Middleware<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState> middleware) {
        afterCommandsExecuting.add(middleware);
    }

    public List<Middleware<MiddlewareArg<U, U, M>, PipelineState>> getBeforeCommandsSearching() {
        return Collections.unmodifiableList(beforeCommandsSearching);
    }
    public List<Middleware<MiddlewareArg<Set<Trigger<U>>, U, M>, PipelineState>> getAfterCommandsSearching() {
        return Collections.unmodifiableList(afterCommandsSearching);
    }
    public List<Middleware<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState>> getBeforeCommandsExecuting() {
        return Collections.unmodifiableList(beforeCommandsExecuting);
    }
    public List<Middleware<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState>> getBeforeCommandExecute() {
        return Collections.unmodifiableList(beforeCommandExecute);
    }
    public List<Middleware<MiddlewareArg<BaseCommand<U, M>, U, M>, PipelineState>> getAfterCommandExecute() {
        return Collections.unmodifiableList(afterCommandExecute);
    }
    public List<Middleware<MiddlewareArg<Set<BaseCommand<U, M>>, U, M>, PipelineState>> getAfterCommandsExecuting() {
        return Collections.unmodifiableList(afterCommandsExecuting);
    }
}
