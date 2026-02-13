package com.Reptir.TelegramJavaBot.CommandsRealization;

import com.Reptir.TelegramJavaBot.CommandLogic.Command;
import com.Reptir.TelegramJavaBot.CommandLogic.InformationForCommand;
import com.Reptir.TelegramJavaBot.utils.TelegramWrappers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AnswerHasMother implements Command {
    @Override
    public String getName() {
        return "AnswerHasMother";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(InformationForCommand info, String[] args)  {
        EditMessageText sendMessage = TelegramWrappers.editMessage(info.getMessage().getChatId(), Integer.valueOf(args[0]), "Не верю нахуй я тебе. но пашел в пизду бля");
        try {
            info.getTgClient().execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
