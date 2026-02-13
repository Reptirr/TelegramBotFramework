package com.Reptir.TelegramJavaBot.MenuRealization;

import com.Reptir.TelegramJavaBot.MenuLogic.InlineKeyboardBuilder;
import com.Reptir.TelegramJavaBot.MenuLogic.KeyboardMenu;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class EmptyMenu implements KeyboardMenu {
    @Override
    public InlineKeyboardMarkup create() {
        return InlineKeyboardBuilder.builder().build();
    }
}
