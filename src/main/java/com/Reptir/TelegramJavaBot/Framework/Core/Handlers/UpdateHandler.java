package com.Reptir.TelegramJavaBot.Framework.Core.Handlers;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.*;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
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
        String commandName;

        if (update.hasMessage() && update.getMessage().hasText()) {                    // Message
            commandName = update.getMessage().getText().split(" ")[0];
        } else if (update.hasEditedMessage() && update.getEditedMessage().hasText()) { // EditedMessage !
            commandName = update.getEditedMessage().getText().split(" ")[0];
        } else if (update.hasCallbackQuery()) {                                        // CallbackQuery
            commandName = update.getCallbackQuery().getData().split(":")[0];
        } else {
            logger.info("Unknown update type. Skipping");
            return;
        }


        Context ctx = new Context(new Messenger(tgClient), update, dialogManager, registryUser);

        commandExecutor.execCommand(commandName, ctx);
    }
}
