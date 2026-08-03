package dev.Reptir.Tafabo.Framework.Handlers;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.CommandLogic.CommandExecutor;
import dev.Reptir.Tafabo.Framework.CommandLogic.CommandRouter;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import dev.Reptir.Tafabo.Framework.dto.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class TafaboApplication<U, M> {
    private final Logger logger = LoggerFactory.getLogger(TafaboApplication.class);

    RegistryThread threadRegistry;
    CommandRouter<U, M> router;
    M messenger;
    CommandExecutor<U, M> commandExecutor;


    public TafaboApplication(RegistryCommand<U, M> commandRegistry, M messenger, CommandExecutor<U, M> executor, RegistryThread registryThread) {
        this.router = new CommandRouter<>(commandRegistry);
        this.messenger = messenger;
        this.commandExecutor = executor;
        this.threadRegistry = registryThread;
    }

    public void consumeUpdate(U update) {
        Context<U, M> ctx = new Context<>(update, messenger);

        Set<BaseCommand<U, M>> matchedCommands = router.getMatched(update);
        commandExecutor.executeAll(matchedCommands, ctx);
    }
}
