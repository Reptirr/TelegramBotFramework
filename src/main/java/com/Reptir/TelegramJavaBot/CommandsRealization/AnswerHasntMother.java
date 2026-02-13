package com.Reptir.TelegramJavaBot.CommandsRealization;

import com.Reptir.TelegramJavaBot.CommandLogic.Command;
import com.Reptir.TelegramJavaBot.CommandLogic.InformationForCommand;
import com.Reptir.TelegramJavaBot.MenuRealization.MotherDeadConditionMenu;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnswerHasntMother implements Command {
    @Override
    public String getName() {
        return "AnswerHasntMother";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(InformationForCommand info, String[] args) {
        InlineKeyboardMarkup markup = new MotherDeadConditionMenu().create();
        SendMessage sendMessage = SendMessage.builder()
                .text("лошара, сдохла или 2 папы?")
                .chatId(info.getMessage().getChatId())
                .replyMarkup(markup)
                .build();

        try {
            info.getTgClient().execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
