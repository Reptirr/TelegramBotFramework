package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;


import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Empties.EmptyCommand;

import java.util.ArrayList;

public class BotBuilder {
    private final String token;
    private final ArrayList<BaseCommand> commands = new ArrayList<>();

    private BotBuilder(String token) {
        this.token = token;
    }

    public static BotBuilder builder(String token) {
        return new BotBuilder(token);
    }

    public BotBuilder command(BaseCommand command) {
        commands.add(command);
        return this;
    }

    public Bot build() {
        this.command(new EmptyCommand()); // скрытый класс

        RegistryCommand registryCommand = new RegistryCommand();

        for (BaseCommand command : commands) {
            registryCommand.register(command.getName(), command);
        }

        return new Bot(token, registryCommand);
    }


}
