package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

/*
* Имеет лист из команд, имеет функ ExecCommand которая запускает команду из аргумента
*/

import com.Reptir.TelegramJavaBot.Framework.Core.RegistryLogic.Registry;

public class TelegramCommandExecutor {
    private final Registry registry;

    public TelegramCommandExecutor(Registry registry) {
        this.registry = registry;
    }

    private void ExecCommand(String commandName, Context ctx, String[] args) {
        BaseCommand command = registry.get(commandName);
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
        BaseCommand command = registry.get(commandName);
        if (command != null && command.isForUserInput()) {
            ExecCommand(commandName, ctx, args);
        }
    }
}
