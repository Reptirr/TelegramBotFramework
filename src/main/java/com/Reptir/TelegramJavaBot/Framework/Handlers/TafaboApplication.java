package com.Reptir.TelegramJavaBot.Framework.Handlers;

import com.Reptir.TelegramJavaBot.Framework.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandRouter;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryThread;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.TafaboAdapter.BotUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class TafaboApplication<U, M> {
    private final Logger logger = LoggerFactory.getLogger(TafaboApplication.class);

    RegistryThread threadRegistry;
    RegistryUser registryUser;
    CommandRouter<U, M> router;
    M messenger;
    CommandExecutor<U, M> commandExecutor;


    public TafaboApplication(RegistryCommand<U, M> commandRegistry, M messenger, CommandExecutor<U, M> executor, RegistryThread registryThread, RegistryUser registryUser) {
        this.router = new CommandRouter<>(commandRegistry);
        this.messenger = messenger;
        this.commandExecutor = executor;
        this.threadRegistry = registryThread;
        this.registryUser = registryUser;
    }

    public void consumeUpdate(U update) {
        Context<U, M> ctx = new Context<>(update, messenger, registryUser);

        registryUser.addBotUserIfNotRegistered(new BotUser()); // добавление юзера в регистр

        Set<BaseCommand<U, M>> matchedCommands = router.getMatched(update);
        commandExecutor.executeAll(matchedCommands, ctx);
    }
}
