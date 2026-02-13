package com.Reptir.TelegramJavaBot.CommandsRealization;

import com.Reptir.TelegramJavaBot.CommandLogic.Command;
import com.Reptir.TelegramJavaBot.CommandLogic.InformationForCommand;
import com.Reptir.TelegramJavaBot.MenuRealization.StartMenu;
import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import java.util.List;


public class StartCommand implements Command {
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
    public void execute(InformationForCommand info, String[] args) {
        TelegramClient tgClient = info.getTgClient();
        InlineKeyboardMarkup markup = new StartMenu().create();

        SendMessage sendMessage = SendMessage.builder()
                .chatId(info.getMessage().getChatId())
                .text("Мама есть? Отвечай блять")
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
