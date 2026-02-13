package com.Reptir.TelegramJavaBot.MenuRealization;

import com.Reptir.TelegramJavaBot.MenuLogic.InlineKeyboardBuilder;
import com.Reptir.TelegramJavaBot.MenuLogic.KeyboardMenu;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.CopyTextButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class StartMenu implements KeyboardMenu {
    @Override
    public InlineKeyboardMarkup create() {
        InlineKeyboardButton AnswerHasntMother = InlineKeyboardButton.builder()
                .text("Да")
                .callbackData("AnswerHasntMother")
                .build();

        InlineKeyboardButton AnswerHasMother = InlineKeyboardButton.builder()
                .text("Нет")
                .callbackData("AnswerHasMother")
                .build();


        return InlineKeyboardBuilder.builder()
                .row(AnswerHasntMother, AnswerHasMother)
                .build();
    }
}
