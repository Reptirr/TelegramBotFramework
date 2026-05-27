package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

public class CommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private final RegistryCommand registryCommand;
    private final RegistryThread registryThread;

    public CommandExecutor(RegistryCommand registryCommand, RegistryThread registryThread) {
        this.registryCommand = registryCommand;
        this.registryThread = registryThread;
    }

    public void execCommand(Context ctx) {
        if (ctx == null) {
            logger.warn("Detected null ctx, skipping");
            return;
        }

        Set<BaseCommand> triggeredCommands = registryCommand.get(ctx.update());

        for (BaseCommand command : triggeredCommands) {
            command.execute(ctx);
        }
    }
}
