package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private final RegistryCommand registryCommand;

    public CommandExecutor(RegistryCommand registryCommand) {
        this.registryCommand = registryCommand;
    }

    public void execCommand(String commandName, Context ctx, String[] args) {
        if (ctx == null) {
            logger.warn("Detected null ctx, skipping");
            return;
        }

        CommandEntry entry = registryCommand.get(commandName);

        if (entry == null) {
            logger.info("Can`t find command '{}', skipping", commandName);
            return;
        }

        if (entry.triggers().contains(ctx.trigger())) {
            entry.command().execute(ctx, args);
        }
    }
}
