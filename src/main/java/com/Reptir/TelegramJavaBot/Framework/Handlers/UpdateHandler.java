package com.Reptir.TelegramJavaBot.Framework.Handlers;

import com.Reptir.TelegramJavaBot.Framework.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandRouter;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.TelegramContext;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryThread;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Telegram.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Telegram.Messenger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import lombok.SneakyThrows;

import java.util.Set;

public class UpdateHandler implements LongPollingSingleThreadUpdateConsumer {
    private final Logger logger = LoggerFactory.getLogger(UpdateHandler.class);

    RegistryThread threadRegistry;
    RegistryUser registryUser;
    CommandRouter router;
    Messenger messenger;
    CommandExecutor commandExecutor;


    public UpdateHandler(RegistryCommand commandRegistry, Messenger messenger, CommandExecutor executor, RegistryThread registryThread, RegistryUser registryUser) {
        this.router = new CommandRouter(commandRegistry);
        this.messenger = messenger;
        this.commandExecutor = executor;
        this.threadRegistry = registryThread;
        this.registryUser = registryUser;
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        Context ctx = new Context(new TelegramContext(update, messenger), registryUser);

        registryUser.addBotUserIfNotRegistered(new BotUser(ctx.user())); // добавление юзера в регистр

        Set<BaseCommand> matchedCommands = router.getMatched(update);
        commandExecutor.executeAll(matchedCommands, ctx);
    }
}
