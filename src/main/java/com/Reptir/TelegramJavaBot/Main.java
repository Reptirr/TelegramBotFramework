package com.Reptir.TelegramJavaBot;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Bot;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotBuilder;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.ActionCommand;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.StartCommand;

import java.util.Map;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // bot by me: @TestBotByReptir_bot (its token in there)
        Bot bot = BotBuilder.builder("8537210836:AAHjOov883WvvYYFutXtzBzea0BTDw8qUHc") // your token bot. you can get it be @BotFather in telegram.
                .command(new ActionCommand()) // you must register your command there
                .command(new StartCommand())
                .build();

        bot.start(); // starting bot
    }
}
