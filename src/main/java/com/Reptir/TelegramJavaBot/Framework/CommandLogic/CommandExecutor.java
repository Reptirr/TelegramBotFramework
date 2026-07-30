package com.Reptir.TelegramJavaBot.Framework.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CommandExecutor<U, M> {
    private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private final RegistryCommand<U, M> registryCommand;
    private final RegistryThread registryThread;

    public CommandExecutor(RegistryCommand<U, M> registryCommand, RegistryThread registryThread) {
        this.registryCommand = registryCommand;
        this.registryThread = registryThread;
    }

    public void executeAll(Set<BaseCommand<U, M>> commands, Context<U, M> ctx) {
        if (ctx == null) {
            logger.warn("Detected null ctx, skipping");
            return;
        }

        for (BaseCommand<U, M> command : commands) {
            registryThread.createThread(() -> command.execute(ctx));
        }
    }
}
