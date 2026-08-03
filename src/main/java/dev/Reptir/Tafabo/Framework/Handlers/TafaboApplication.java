package dev.Reptir.Tafabo.Framework.Handlers;

import dev.Reptir.Tafabo.Framework.CommandLogic.BaseCommand;
import dev.Reptir.Tafabo.Framework.CommandLogic.CommandRouter;
import dev.Reptir.Tafabo.Framework.Registries.RegistryCommand;
import dev.Reptir.Tafabo.Framework.Registries.RegistryThread;
import dev.Reptir.Tafabo.Framework.dto.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class TafaboApplication<U, M> {
    RegistryThread threadRegistry;
    CommandRouter<U, M> router;
    M messenger;


    public TafaboApplication(RegistryCommand<U, M> commandRegistry, M messenger, RegistryThread registryThread) {
        this.router = new CommandRouter<>(commandRegistry);
        this.messenger = messenger;
        this.threadRegistry = registryThread;
    }

    public void consumeUpdate(U update) {
        Context<U, M> ctx = new Context<>(update, messenger);

        Set<BaseCommand<U, M>> matchedCommands = router.getMatched(update);

        matchedCommands.forEach(command -> {
            command.execute(ctx);
        });
    }
}
