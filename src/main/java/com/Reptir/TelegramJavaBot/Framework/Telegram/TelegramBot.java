package com.Reptir.TelegramJavaBot.Framework.Telegram;

import com.Reptir.TelegramJavaBot.Framework.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandEntry;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Handlers.UpdateHandler;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryThread;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.ThreadLogic.ThreadId;
import com.Reptir.TelegramJavaBot.Framework.TriggerLogic.Trigger;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;
import java.util.concurrent.Future;

public class TelegramBot {
    private final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final String token;
    private boolean isStarted = false;

    private final RegistryCommand registryCommand;
    private final RegistryThread registryThread;
    private TelegramBotsLongPollingApplication app;
    private final RegistryUser registryUser = new RegistryUser();

    @Getter
    private final Messenger messenger;

    public TelegramBot(String token) {
        this.token = token;
        this.registryCommand = new RegistryCommand();
        registryThread = new RegistryThread();
        messenger = new Messenger(new OkHttpTelegramClient(token));
    }

    public void start() {
        if (!isStarted) {

            app = new TelegramBotsLongPollingApplication();
            CommandExecutor executor = new CommandExecutor(registryCommand, registryThread);
            UpdateHandler updateHandler = new UpdateHandler(registryCommand, messenger, executor, registryThread, registryUser);


            try {
                app.registerBot(token, updateHandler);
                isStarted = true;
            } catch (TelegramApiException e) {
                logger.error("Failed to start bot", e);
            }

        }
    }

    public void stop() {
        if (isStarted) {

            try {
                app.close();
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
    public Map<Long, BotUser> getUsers() {
        return registryUser.getUsers();
    }

    public void addCommand(Trigger trigger, BaseCommand command) {
        registryCommand.register(trigger, new CommandEntry(command));
    }
}
