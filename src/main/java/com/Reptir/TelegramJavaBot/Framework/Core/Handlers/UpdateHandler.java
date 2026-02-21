package com.Reptir.TelegramJavaBot.Framework.Core.Handlers;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.TelegramCommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryThread;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import lombok.SneakyThrows;

import java.util.Arrays;

@Component
public class UpdateHandler implements LongPollingSingleThreadUpdateConsumer {
    RegistryCommand commandRegistry;
    RegistryThread threadRegistry;
    RegistryUser registryUser;
    TelegramClient tgClient;
    TelegramCommandExecutor commandExecutor;


    public UpdateHandler(RegistryCommand commandRegistry, TelegramClient tgClient, TelegramCommandExecutor executor, RegistryThread registryThread, RegistryUser registryUser) {
        this.commandRegistry = commandRegistry;
        this.tgClient = tgClient;
        this.commandExecutor = executor;
        this.threadRegistry = registryThread;
        this.registryUser = registryUser;
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().getFrom() != null) {
            registryUser.addBotUserIfNotInRegistry(new BotUser(update.getMessage().getFrom()));
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            registryUser.addBotUserIfNotInRegistry(new BotUser(update.getCallbackQuery().getFrom()));
        }

        if (update.hasMessage()) {

            Message message = update.getMessage();

            String[] parts = message.getText().split(" ");
            String input = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            Context ctx = new Context(message, tgClient, null);

            threadRegistry.createThread("", () -> commandExecutor.ExecByInput(input, ctx, args));
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage() instanceof Message message) {
                String callbackData = update.getCallbackQuery().getData();
                String[] parts = callbackData.split(":");

                Context ctx = new Context(message, tgClient, update.getCallbackQuery());
                String commandName = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                System.out.println("handle: " + commandName);
                threadRegistry.createThread(threadRegistry.getNewId(), () -> commandExecutor.ExecByInternal(commandName, ctx, args));
            } else {
                // logging in future
            }
        }
    }
}
