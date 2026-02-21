package com.Reptir.TelegramJavaBot;

import com.Reptir.TelegramJavaBot.Framework.Core.Registries.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Bot;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotBuilder;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.ActionCommand;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.StartCommand;

import java.util.Map;

/*
* TODO:
*  1. Система диалогов:
*  - Регистр <String userId, UserState>
*  - UserState - это интерфейс с 3 полями: String currentAction, long timestamp (seconds), BaseCommand ExecutionCommand
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Bot bot = BotBuilder.builder("8537210836:AAFIG9jOQ4sVfuJChizISnbxBPgodfzjPn0") // your token bot. you can get it be @BotFather in telegram.
                .command(new ActionCommand()) // you must register your command there
                .command(new StartCommand())
                .build();

        Thread t = new Thread(bot::start); // starting bot
        t.start();

        while (true) {
            Map<Long, BotUser> users = bot.getUsers();

            System.out.println(users);

            Thread.sleep(1000);
        }

    }
}
