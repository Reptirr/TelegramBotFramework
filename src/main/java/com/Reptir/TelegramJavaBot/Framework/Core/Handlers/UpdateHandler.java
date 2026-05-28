package com.Reptir.TelegramJavaBot.Framework.Core.Handlers;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic.TelegramContext;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.*;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import lombok.SneakyThrows;

import java.util.Set;

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
        Context ctx = new Context(new TelegramContext(update, new Messenger(tgClient)), dialogManager, registryUser);
        registryUser.addBotUserIfNotRegistered(new BotUser(ctx.user())); // добавление юзера в регистр
        BotUser currentUser = registryUser.getBotUser(ctx.user().getId());

        if (currentUser.getDialogState() != null) {          // исполнение диалога
            dialogManager.executeDialogIfExists(currentUser.getId(), ctx);
            return;
        }

        Set<BaseCommand> matchedCommands = commandRegistry.getMatched(ctx);
        commandExecutor.executeAll(matchedCommands, ctx);
    }
}
