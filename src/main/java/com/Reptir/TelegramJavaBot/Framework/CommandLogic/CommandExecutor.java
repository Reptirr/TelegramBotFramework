package com.Reptir.TelegramJavaBot.Framework.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private final RegistryCommand registryCommand;
    private final RegistryThread registryThread;

    public CommandExecutor(RegistryCommand registryCommand, RegistryThread registryThread) {
        this.registryCommand = registryCommand;
        this.registryThread = registryThread;
    }

    public void executeAll(Set<BaseCommand> commands, Context ctx) {
        if (ctx == null) {
            logger.warn("Detected null ctx, skipping");
            return;
        }

        for (BaseCommand command : commands) {
            registryThread.createThread(() -> command.execute(ctx));
        }
    }
}
