package com.Reptir.TelegramJavaBot.Framework.Empties;

import com.Reptir.TelegramJavaBot.Framework.MenuLogic.InlineKeyboardBuilder;
import com.Reptir.TelegramJavaBot.Framework.MenuLogic.BaseMenu;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class EmptyMenu implements BaseMenu {
    @Override
    public InlineKeyboardMarkup create() {
        return InlineKeyboardBuilder.builder().build();
    }
}
