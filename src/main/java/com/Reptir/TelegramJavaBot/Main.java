package com.Reptir.TelegramJavaBot;

import com.Reptir.TelegramJavaBot.Framework.Core.Registries.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Bot;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotBuilder;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.ActionCommand;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.DialogCommand;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.StartCommand;

import java.util.Map;

/*
* TODO:
*  1. Система диалогов:
*  - UserState - это интерфейс с 3 полями: String currentAction, long timestamp (seconds), BaseCommand ExecutionCommand
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bot bot = BotBuilder.builder("TOKEN") // your token bot. you can get it in @BotFather in telegram.
                .command(new ActionCommand()) // you must register your command there
                .command(new StartCommand())
                .command(new DialogCommand())
                .build();

        Thread t = new Thread(bot::start); // starting bot
        t.start();
    }
}
