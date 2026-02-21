package com.Reptir.TelegramJavaBot;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Bot;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotBuilder;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.ActionCommand;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.StartCommand;

/*
* TODO:
*  1. Система диалогов:
*  - Регистр <String userId, UserState>
*  - UserState - это интерфейс с 3 полями: String currentAction, long timestamp (seconds), BaseCommand ExecutionCommand
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bot bot = BotBuilder.builder("YOUR_TOKEN") // your token bot. you can get it be @BotFather in telegram.
                .command(new ActionCommand()) // you must register your command there
                .command(new StartCommand())
                .build();

        bot.start(); // starting bot
    }
}
