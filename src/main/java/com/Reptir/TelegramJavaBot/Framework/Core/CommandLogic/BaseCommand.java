package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface BaseCommand {
    String getName();
    boolean isForUserInput();


    void execute(Context ctx, String[] args) ;

    default void executeAlert(Context ctx) {
        AnswerCallbackQuery answer = AnswerCallbackQuery.builder()
                .callbackQueryId(ctx.getCallback().getId())
                .text("")
                .showAlert(false)
                .build();

        try {
            ctx.getTgClient().execute(answer);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


}
