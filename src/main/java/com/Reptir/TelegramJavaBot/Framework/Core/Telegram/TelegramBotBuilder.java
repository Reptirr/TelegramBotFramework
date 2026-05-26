package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;


import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Empties.EmptyCommand;

public class TelegramBotBuilder {
    TelegramBot telegramBot;

    private TelegramBotBuilder(String token) {
        this.telegramBot = new TelegramBot(token);
    }

    public static TelegramBotBuilder builder(String token) {
        return new TelegramBotBuilder(token);
    }

    public TelegramBotBuilder addCommand(String name, BaseCommand command) {
        telegramBot.addCommand(name, command);
        return this;
    }

    public TelegramBot build() {
        this.addCommand("", new EmptyCommand()); // скрытый класс для пустых колбеков

        return telegramBot;
    }


}
