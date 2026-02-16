package com.Reptir.TelegramJavaBot.Realizations.MenuRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.MenuLogic.BaseMenu;
import com.Reptir.TelegramJavaBot.Framework.Core.MenuLogic.InlineKeyboardBuilder;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.TelegramWrappers;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class JustMenu implements BaseMenu {

    @Override
    public InlineKeyboardMarkup create() {
        InlineKeyboardButton btn = TelegramWrappers.button("a u nigger?", "nigger");

        return InlineKeyboardBuilder.builder()
                .row("hi", "hello")
                .row(btn)
                .build();

    }
}
