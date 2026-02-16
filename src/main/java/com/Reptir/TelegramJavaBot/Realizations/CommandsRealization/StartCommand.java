package com.Reptir.TelegramJavaBot.Realizations.CommandsRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Realizations.MenuRealization.JustMenu;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


public class StartCommand implements BaseCommand {
    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public boolean isForUserInput() {
        return true;
    }

    @SneakyThrows
    @Override
    public void execute(Context ctx, String[] args) {
        TelegramClient tgClient = ctx.getTgClient();
        InlineKeyboardMarkup markup = new JustMenu().create();



        SendMessage sendMessage = SendMessage.builder()
                .chatId(ctx.getMessage().getChatId())
                .text("What`s up? brooooooooooo")
                .replyMarkup(markup)
                .build();

        try {
            tgClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            // logging in future
        }
    }

}
