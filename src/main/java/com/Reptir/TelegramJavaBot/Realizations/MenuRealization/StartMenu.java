package com.Reptir.TelegramJavaBot.Realizations.MenuRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.MenuLogic.BaseMenu;
import com.Reptir.TelegramJavaBot.Framework.Core.MenuLogic.InlineKeyboardBuilder;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

// it`s just a class-builder of InlineKeyboardMarkup
// You must use it yourself or not
public class StartMenu implements BaseMenu {
    @Override
    public InlineKeyboardMarkup create() {
        InlineKeyboardButton btn = InlineKeyboardButton.builder()
                .text("Some action")
                .callbackData("action1")
                .build();

        return InlineKeyboardBuilder.builder()
                .row("button 1", "button 2")
                .row(btn)
                .build();
    }
}
