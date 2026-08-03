package com.Reptir.Tafabo.Framework.CommandLogic;

import com.Reptir.Tafabo.Framework.Registries.RegistryThread;
import com.Reptir.Tafabo.Framework.dto.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CommandExecutor<U, M> {
    private final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private final RegistryThread registryThread;

    public CommandExecutor(RegistryThread registryThread) {
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
