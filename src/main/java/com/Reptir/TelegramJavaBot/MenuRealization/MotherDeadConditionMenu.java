package com.Reptir.TelegramJavaBot.MenuRealization;

import com.Reptir.TelegramJavaBot.MenuLogic.InlineKeyboardBuilder;
import com.Reptir.TelegramJavaBot.MenuLogic.KeyboardMenu;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class MotherDeadConditionMenu implements KeyboardMenu {
    @Override
    public InlineKeyboardMarkup create() {
        InlineKeyboardButton buttonMotherDead = InlineKeyboardButton.builder()
                .text("Мама мертва")
                .callbackData("AnswerMotherDead")
                .build();

        InlineKeyboardButton buttonTwoFathers = InlineKeyboardButton.builder()
                .text("Две папы")
                .callbackData("AnswerTwoFathers")
                .build();

        return InlineKeyboardBuilder.builder()
                .row(buttonMotherDead, buttonTwoFathers)
                .build();
    }
}
