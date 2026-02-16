package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.TelegramCommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.Handlers.UpdateHandler;
import com.Reptir.TelegramJavaBot.Framework.Core.RegistryLogic.Registry;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Bot {
    private final String token;
    private final Registry registry;
    private boolean isStarted = false;
    private TelegramBotsLongPollingApplication app;

    public Bot(String token, Registry registry) {
        this.token = token;
        this.registry = registry;
    }

    public void start() {
        if (!isStarted) {
            isStarted = true;

            app = new TelegramBotsLongPollingApplication();
            TelegramClient tgClient = new OkHttpTelegramClient(token);
            TelegramCommandExecutor executor = new TelegramCommandExecutor(registry);

            try {
                app.registerBot(token, new UpdateHandler(registry, tgClient, executor));
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
                // logging in future
            }

        }
    }

    public void stop() {
        if (isStarted) {
            isStarted = false;
            try {
                app.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
                // logging in future
            }

        }
    }

    public void addCommand(BaseCommand command) {
        registry.register(command.getName(), command);
    }

    public void removeCommand(BaseCommand command) {
        registry.remove(command.getName());
    }
}
