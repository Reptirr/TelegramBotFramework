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
            logger.warn("Detected null ctx, skipping");
            return;
        }

        CommandEntry entry = registryCommand.get(commandName);

        if (entry == null) {
            logger.info("Can`t find command '{}', skipping", commandName);
            return;
        }

        // определение какого типа апдейт
        CommandTrigger messageTrigger;
        if (ctx.getUpdate().hasMessage()) {
            messageTrigger = CommandTrigger.USER_INPUT;
        } else if (ctx.getUpdate().hasCallbackQuery()) {
            messageTrigger = CommandTrigger.CALLBACK;
        } else if (ctx.getUpdate().hasEditedMessage()) {
            messageTrigger = CommandTrigger.MESSAGE_EDITED;
        } else {
            messageTrigger = CommandTrigger.UNKNOWN;
        }

        if (entry.triggers().contains(messageTrigger)) {
            entry.command().execute(ctx, args);
        }
    }

    public void ExecByInternal(String commandName, Context ctx, String[] args)  {
        CommandEntry entry = registryCommand.get(commandName);

        if (entry.triggers().contains(CommandTrigger.CALLBACK))
            ExecCommand(commandName, ctx, args);
    }

    public void ExecByInput(String commandName, Context ctx, String[] args)  {
        CommandEntry entry = registryCommand.get(commandName);

        if (entry.triggers().contains(CommandTrigger.USER_INPUT))
            ExecCommand(commandName, ctx, args);
    }
}
