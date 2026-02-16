package com.Reptir.TelegramJavaBot.Framework.Core.Handlers;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.TelegramCommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.RegistryLogic.Registry;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import lombok.SneakyThrows;

import java.util.Arrays;

@Component
public class UpdateHandler implements LongPollingSingleThreadUpdateConsumer {
    Registry commandRegistry;
    TelegramClient tgClient;
    TelegramCommandExecutor commandExecutor;

    public UpdateHandler(Registry commandRegistry, TelegramClient tgClient, TelegramCommandExecutor executor) {
        this.commandRegistry = commandRegistry;
        this.tgClient = tgClient;
        this.commandExecutor = executor;
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {

            Message message = update.getMessage();

            String[] parts = message.getText().split(" ");
            String input = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);

            Context ctx = new Context(message, tgClient, null);

            commandExecutor.ExecByInput(input, ctx, args);
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage() instanceof Message message) {
                String callbackData = update.getCallbackQuery().getData();
                String[] parts = callbackData.split(":");

                Context ctx = new Context(message, tgClient, update.getCallbackQuery());
                String commandName = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                System.out.println("handle: " + commandName);
                commandExecutor.ExecByInternal(commandName, ctx, args);
            } else {
                // logging in future
            }
        }
    }
}
