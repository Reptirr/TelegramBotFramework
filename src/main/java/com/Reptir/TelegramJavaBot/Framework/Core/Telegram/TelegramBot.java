package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandEntry;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandTrigger;
import com.Reptir.TelegramJavaBot.Framework.Core.Handlers.UpdateHandler;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.*;
import com.Reptir.TelegramJavaBot.Framework.Core.ThreadLogic.ThreadId;
import com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic.TimeoutService;
import com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic.TimeoutThreadManager;
import com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

public class TelegramBot {
    private final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final String token;
    private boolean isStarted = false;

    private final RegistryCommand registryCommand;
    private final RegistryThread registryThread;
    private TelegramBotsLongPollingApplication app;
    private final RegistryUser registryUser = new RegistryUser();
    private final TimeoutService timeoutService = new TimeoutService(10, registryUser);
    private final TimeoutThreadManager timeoutThreadManager = new TimeoutThreadManager(timeoutService, 1);

    public TelegramBot(String token) {
        this.token = token;
        this.registryCommand = new RegistryCommand();
        registryThread = new RegistryThread();
    }

    public void start() {
        if (!isStarted) {

            app = new TelegramBotsLongPollingApplication();
            TelegramClient tgClient = new OkHttpTelegramClient(token);
            CommandExecutor executor = new CommandExecutor(registryCommand, registryThread);
            UpdateHandler updateHandler = new UpdateHandler(registryCommand, tgClient, executor, registryThread, registryUser);

            timeoutThreadManager.startChecking();

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
                timeoutThreadManager.stopChecking();
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

    public void setTimeoutDialog(short seconds) {
        timeoutService.setTimeout(seconds);
    }
}
