package com.Reptir.TelegramJavaBot.Framework.TafaboAdapter;

import com.Reptir.TelegramJavaBot.Framework.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Handlers.TafaboApplication;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryThread;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.ThreadLogic.ThreadId;
import com.Reptir.TelegramJavaBot.Framework.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public abstract class Adapter<U, M> {
    private final Logger logger = LoggerFactory.getLogger(Adapter.class);
    private boolean isStarted = false;

    private final RegistryCommand<U, M> registryCommand = new RegistryCommand<>();
    private final RegistryThread registryThread = new RegistryThread();
    private final RegistryUser registryUser = new RegistryUser();
    private TafaboApplication<U, M> tafaboApplication;

    private final M messenger;

    public Adapter(M messenger) {
        this.messenger = messenger;
    }

    protected abstract void onStart() throws TelegramApiException;
    protected abstract void onStop() throws TelegramApiException;

    protected void onUpdate(U update) {
        tafaboApplication.consumeUpdate(update);
    }

    public void start() {
        if (!isStarted) {

            try {
                onStart();
                isStarted = true;
            } catch (Exception e) {
                logger.error("Failed to start bot", e);
            }

            CommandExecutor<U, M> executor = new CommandExecutor<>(registryCommand, registryThread);
            tafaboApplication = new TafaboApplication<>(registryCommand, messenger, executor, registryThread, registryUser);

        }
    }

    public void stop() {
        if (isStarted) {
            try {
                onStop();
                isStarted = false;
                registryThread.shutdown();
            } catch (Exception e) {
                logger.error("Failed to stop bot", e);
            }

        }
    }

    public Map<ThreadId, Future<?>> getThreads() {
        return registryThread.getThreads();
    }
    public Set<BotUser> getUsers() {
        return registryUser.getUsers();
    }

    public void addCommand(Trigger<U> trigger, BaseCommand<U, M> command) {
        registryCommand.register(trigger, command);
    }

}
