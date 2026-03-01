package example;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Bot;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotBuilder;
import example.Realizations.CommandsRealization.ActionCommand;
import example.Realizations.CommandsRealization.DialogCommand;
import example.Realizations.CommandsRealization.StartCommand;


/*
* TODO:
*  1. Система диалогов:
*  - UserState - это интерфейс с 3 полями: String currentAction, long timestamp (seconds), BaseCommand ExecutionCommand
*/

public class Main {
    public static void main(String[] args) {
        Bot bot = BotBuilder.builder("your token") // your token bot. you can get it in @BotFather in telegram.
                .command(new ActionCommand()) // you must register your command there
                .command(new StartCommand())
                .command(new DialogCommand())
                .build();

        Thread t = new Thread(bot::start); // starting bot
        t.start();
    }
}
