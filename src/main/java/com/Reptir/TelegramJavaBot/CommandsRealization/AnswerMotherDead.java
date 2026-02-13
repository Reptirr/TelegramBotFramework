package com.Reptir.TelegramJavaBot.CommandsRealization;

import com.Reptir.TelegramJavaBot.CommandLogic.Command;
import com.Reptir.TelegramJavaBot.CommandLogic.InformationForCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnswerMotherDead implements Command {

    @Override
    public String getName() {
        return "AnswerMotherDead";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(InformationForCommand info, String[] args)  {
        SendMessage sendMessage = SendMessage.builder()
                .text("сочуствую, хаха")
                .chatId(info.getMessage().getChatId())
                .build();

        try {
            info.getTgClient().execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
