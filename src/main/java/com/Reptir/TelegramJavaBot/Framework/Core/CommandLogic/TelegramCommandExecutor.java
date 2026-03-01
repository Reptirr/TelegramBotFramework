package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;

public class TelegramCommandExecutor {
    private final RegistryCommand registryCommand;

    public TelegramCommandExecutor(RegistryCommand registryCommand) {
        this.registryCommand = registryCommand;
    }

    private void ExecCommand(String commandName, Context ctx, String[] args) {
        BaseCommand command = registryCommand.get(commandName);
        if (command != null) {
            command.execute(ctx, args);
            if (ctx.getCallback() != null) {
                command.executeAlert(ctx);
            }
        }
    }

    public void ExecByInternal(String commandName, Context ctx, String[] args)  {
        ExecCommand(commandName, ctx, args);

    }

    public void ExecByInput(String commandName, Context ctx, String[] args)  {
        BaseCommand command = registryCommand.get(commandName);
        if (command != null && command.isForUserInput()) {
            ExecCommand(commandName, ctx, args);
        }
    }
}
