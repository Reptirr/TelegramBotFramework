package com.Reptir.TelegramJavaBot;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Bot;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotBuilder;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.JustButton;
import com.Reptir.TelegramJavaBot.Realizations.CommandsRealization.StartCommand;

public class Main {
    public static void main(String[] args) {
        Bot bot = BotBuilder.builder("8206356664:AAFFVw99AzeG2YFxgOnO4f9VAZFjRTKr_z0")
                .command(new StartCommand())
                .command(new JustButton())
                .build();

        bot.start();
    }
}
