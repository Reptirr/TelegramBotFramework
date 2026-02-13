package com.Reptir.TelegramJavaBot.MenuLogic;

import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;

import java.util.*;

public class InlineKeyboardBuilder {
    private final List<InlineKeyboardRow> rows = new ArrayList<>();

    public static InlineKeyboardBuilder builder() {
        return new InlineKeyboardBuilder();
    }

    private InlineKeyboardBuilder addRow(InlineKeyboardRow row) {
        rows.add(row);
        return this;
    }


    public InlineKeyboardBuilder row(@NotNull InlineKeyboardButton... buttons) {
        if (buttons.length == 0) {
            throw new IllegalArgumentException("Row must contain at least one button");
        }

        InlineKeyboardRow row = new InlineKeyboardRow();

        for (InlineKeyboardButton button : buttons) {
            if (button.getCallbackData() == null || Objects.equals(button.getCallbackData(), "")) {
                button.setCallbackData("callbackNotForHandle");
            }
            row.add(button);
        }

        return addRow(row);
    }

    public InlineKeyboardBuilder row(@NotNull String... namesOfButtons) {
        if (namesOfButtons.length == 0) {
            throw new IllegalArgumentException("Row must contain at least one button");
        }
        InlineKeyboardRow row = new InlineKeyboardRow();

        for (String name : namesOfButtons) {
            InlineKeyboardButton button = new InlineKeyboardButton(name);
            button.setCallbackData("callbackNotForHandle");

            row.add(button);
        }

        return addRow(row);
    }

    public InlineKeyboardMarkup build() {
        return new InlineKeyboardMarkup(new ArrayList<>(rows));
    }

}
