package com.Reptir.TelegramJavaBot.Framework.Core.Handlers;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.*;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import lombok.SneakyThrows;

import java.util.Arrays;

public class UpdateHandler implements LongPollingSingleThreadUpdateConsumer {
    private final Logger logger = LoggerFactory.getLogger(UpdateHandler.class);
    RegistryCommand commandRegistry;
    RegistryThread threadRegistry;
    RegistryUser registryUser;

    TelegramClient tgClient;
    CommandExecutor commandExecutor;
    DialogManager dialogManager;


    public UpdateHandler(RegistryCommand commandRegistry, TelegramClient tgClient, CommandExecutor executor, RegistryThread registryThread, RegistryUser registryUser) {
        this.commandRegistry = commandRegistry;
        this.tgClient = tgClient;
        this.commandExecutor = executor;
        this.threadRegistry = registryThread;
        this.registryUser = registryUser;

        this.dialogManager = new DialogManager(registryUser);
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().getFrom() != null) {
            registryUser.addBotUserIfNotRegistered(new BotUser(update.getMessage().getFrom()));         // добавление юзера в регистр
        } else if (update.hasCallbackQuery() && update.getCallbackQuery().getFrom() != null) {
            registryUser.addBotUserIfNotRegistered(new BotUser(update.getCallbackQuery().getFrom()));
        }

        if (update.hasMessage() && !update.getMessage().getText().isBlank()) {             // обработка сообщений

            Message message = update.getMessage();

            Context ctx = new Context(message, tgClient, null, dialogManager, registryUser);
            String[] parts = message.getText().split(" ");
            String input = parts[0];
            String[] args = Arrays.copyOfRange(parts, 1, parts.length);


            if (dialogManager.executeDialogIfExists(ctx.getMessage().getFrom().getId(), ctx)) return; // диалоги

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
