package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.TelegramCommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.Handlers.UpdateHandler;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.*;
import com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic.TimeoutService;
import com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic.TimeoutThreadManager;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Map;
import java.util.concurrent.Future;

public class Bot {
    private final String token;
    private boolean isStarted = false;

    private final RegistryCommand registryCommand;
    private final RegistryThread registryThread;
    private TelegramBotsLongPollingApplication app;
    private final RegistryUser registryUser = new RegistryUser();
    private final RegistryDialogState registryDialogState = new RegistryDialogState();
    private final TimeoutService timeoutService = new TimeoutService(registryDialogState, 10);
    private final TimeoutThreadManager timeoutThreadManager = new TimeoutThreadManager(timeoutService, 1);

    public Bot(String token, RegistryCommand registryCommand) {
        this.token = token;
        this.registryCommand = registryCommand;
        registryThread = new RegistryThread();
    }

    public void start() {
        if (!isStarted) {

            app = new TelegramBotsLongPollingApplication();
            TelegramClient tgClient = new OkHttpTelegramClient(token);
            TelegramCommandExecutor executor = new TelegramCommandExecutor(registryCommand);

            timeoutThreadManager.startChecking();

            try {
                app.registerBot(token, new UpdateHandler(registryCommand, tgClient, executor, registryThread, registryUser, registryDialogState));
                isStarted = true;
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
                // logging in future
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
                throw new RuntimeException(e);
                // logging in future
            }

        }
    }

    public Map<String, Future<?>> getThreads() {
        return registryThread.getThreads();
    }

    public Map<Long, BotUser> getUsers() {
        return registryUser.getUsers();
    }

    public void addCommand(BaseCommand command) {
        registryCommand.register(command.getName(), command);
    }

    public void removeCommand(BaseCommand command) {
        registryCommand.remove(command.getName());
    }

    public void setTimeoutDialog(short seconds) {
        timeoutService.setTimeout(seconds);
    }
}
