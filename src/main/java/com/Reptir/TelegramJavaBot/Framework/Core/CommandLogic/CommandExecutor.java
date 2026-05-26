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

    private void ExecCommand(String commandName, Context ctx, String[] args) {
        if (ctx == null) {
            logger.warn("Detected null ctx");
        }

        BaseCommand command = registryCommand.get(commandName);
        if (command != null) {
            logger.debug("Execute command: {}", commandName);

            command.execute(ctx, args);

            if (ctx == null) return;
            if (ctx.getCallback() != null) {
                logger.debug("Execute alert command: {}", commandName);
                command.executeAlert(ctx);
            }
        }

        BaseCommand defaultCommand = registryCommand.getDefaultCommand();
        if (defaultCommand != null) {
            logger.info("Executing default command: command '{}' not found", commandName);
            defaultCommand.execute(ctx, args);

            if (ctx == null) return;
            if (ctx.getCallback() != null) {
                logger.debug("Execute alert default command");
                defaultCommand.executeAlert(ctx);
            }
        } else {
            logger.info("Cannot find command: '{}'", commandName);
        }
    }

    public void ExecByInternal(String commandName, Context ctx, String[] args)  {
        ExecCommand(commandName, ctx, args);
    }

    public void ExecByInput(String commandName, Context ctx, String[] args)  {
        BaseCommand command = registryCommand.get(commandName);
        if (command.isForUserInput()) {
            ExecCommand(commandName, ctx, args);
        }
    }
}
