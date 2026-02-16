package com.Reptir.TelegramJavaBot.Framework.Core.Empties;

import com.Reptir.TelegramJavaBot.Framework.Core.MenuLogic.InlineKeyboardBuilder;
import com.Reptir.TelegramJavaBot.Framework.Core.MenuLogic.BaseMenu;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class EmptyMenu implements BaseMenu {
    @Override
    public InlineKeyboardMarkup create() {
        return InlineKeyboardBuilder.builder().build();
    }
}
