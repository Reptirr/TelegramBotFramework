package com.Reptir.TelegramJavaBot.Realizations.CommandsRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand; // Standard class of command
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Realizations.MenuRealization.StartMenu;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

// Just a command
public class StartCommand implements BaseCommand {
    @Override
    public String getName() { // name which comand will execute
        return "/start";
    }

    @Override
    public boolean isForUserInput() { // can command execute by user input
        return true;
    }

    @Override
    public void execute(Context ctx, String[] args) { // what will happen when command execute
        SendMessage sendMessage = SendMessage.builder()
                .text("Hi! it`s a start command, " + ctx.getMessage().getFrom().getFirstName() + ". ")
                .chatId(ctx.getMessage().getChatId())
                .replyMarkup(new StartMenu().create()) // it`s an inline keyboard
                .build();

        try {
            ctx.getTgClient().execute(sendMessage);
        } catch (TelegramApiException e) {
            System.out.println("Some problems in command \"StartCommand\": " + e);
        }
    }
}
