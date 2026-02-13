package com.Reptir.TelegramJavaBot.CommandsRealization;

import com.Reptir.TelegramJavaBot.CommandLogic.Command;
import com.Reptir.TelegramJavaBot.CommandLogic.InformationForCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnswerTwoFathers implements Command {
    @Override
    public String getName() {
        return "AnswerTwoFathers";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(InformationForCommand info, String[] args)  {
        SendMessage sendMessage = SendMessage.builder()
                .text("ебать ты че нахуй, совсем блять. сжечь тебя надо нахуй")
                .chatId(info.getMessage().getChatId())
                .build();

        try {
            info.getTgClient().execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
