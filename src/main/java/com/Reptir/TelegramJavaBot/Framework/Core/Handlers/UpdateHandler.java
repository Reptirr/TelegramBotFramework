package com.Reptir.TelegramJavaBot.Framework.Core.Handlers;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.TelegramCommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.*;
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
    RegistryDialogState registryDialogState;

    TelegramClient tgClient;
    TelegramCommandExecutor commandExecutor;
    DialogManager dialogManager;


    public UpdateHandler(RegistryCommand commandRegistry, TelegramClient tgClient, TelegramCommandExecutor executor, RegistryThread registryThread, RegistryUser registryUser, RegistryDialogState registryDialogState) {
        this.commandRegistry = commandRegistry;
        this.tgClient = tgClient;
        this.commandExecutor = executor;
        this.threadRegistry = registryThread;
        this.registryUser = registryUser;

        this.registryDialogState = registryDialogState;
        this.dialogManager = new DialogManager(registryDialogState);
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().getFrom() != null) {
            registryUser.addBotUserIfNotInRegistry(new BotUser(update.getMessage().getFrom()));         // добавление юзера в регистр
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            registryUser.addBotUserIfNotInRegistry(new BotUser(update.getCallbackQuery().getFrom()));
        }

        if (update.hasMessage() && !update.getMessage().getText().isBlank()) {             // обработка сообщений

            Message message = update.getMessage();

            Context ctx = new Context(message, tgClient, null, dialogManager, registryUser);
            String[] parts = message.getText().split(" ");
            String input = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);


            if (dialogManager.executeDialogIfExists(ctx)) return; // диалоги

            threadRegistry.createThread(() -> commandExecutor.ExecByInput(input, ctx, args)); // команда

        } else if (update.hasCallbackQuery()) {                                            // обработка callback
            if (update.getCallbackQuery().getMessage() instanceof Message message) {
                String callbackData = update.getCallbackQuery().getData();
                String[] parts = callbackData.split(":");

                Context ctx = new Context(message, tgClient, update.getCallbackQuery(), dialogManager, registryUser);
                String commandName = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                threadRegistry.createThread(() -> commandExecutor.ExecByInternal(commandName, ctx, args));
            }
        }
    }
}
