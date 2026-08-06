package dev.Reptir.Tafabo.Framework.AdapterLogic;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.Handlers.TafaboApplication;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareRegistrator;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import dev.Reptir.Tafabo.Framework.ThreadLogic.ThreadId;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Future;

public abstract class TafaboAdapter<U, M> {
    private final Logger logger = LoggerFactory.getLogger(TafaboAdapter.class);
    private boolean isStarted = false;

    private final RegistryCommand<U, M> registryCommand = new RegistryCommand<>();
    private final RegistryThread registryThread = new RegistryThread();
    private TafaboApplication<U, M> tafaboApplication;

    private final M messenger;

    public TafaboAdapter(M messenger) {
        this.messenger = messenger;
        this.tafaboApplication = new TafaboApplication<>(registryCommand, messenger, registryThread);
    }

    protected abstract void onStart();
    protected abstract void onStop();

    protected void onUpdate(U update) {
        tafaboApplication.consumeUpdate(update);
    }

    public void start() {
        if (isStarted) {
            return;
        }

        try {
            onStart();
            isStarted = true;
        } catch (Exception e) {
            logger.error("Failed to start bot due to onStart", e);
        }
    }

    public void stop() {
        if (!isStarted) {
            return;
        }

        try {
            onStop();
        } catch (Exception e) {
            logger.error("Failed to stop bot due to onStop", e);
        } finally {
            isStarted = false;
            registryThread.shutdown();
        }
    }

    public boolean running() {
        return isStarted;
    }

    public Map<ThreadId, Future<?>> getThreads() {
        return registryThread.getThreads();
    }

    public void addCommand(Trigger<U> trigger, BaseCommand<U, M> command) {
        registryCommand.register(trigger, command);
    }

    public void addMiddleware(MiddlewareRegistrator<U, M> middlewareRegistrator) {
        tafaboApplication.addMiddleware(middlewareRegistrator);
    }

}
