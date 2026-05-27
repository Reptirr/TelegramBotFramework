package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Triggers {
    public static Trigger startsWith(String text) {
        return new Trigger() {
            @Override
            public boolean match(Update update) {
                if (!update.hasMessage()) return false;
                return update.getMessage().getText().startsWith(text);
            }
        };
    }

    public static Trigger firstWordEquals(String text) {
        return new Trigger() {
            @Override
            public boolean match(Update update) {
                if (!update.hasMessage()) return false;
                return update.getMessage().getText().split(" ")[0].equals(text);
            }
        };
    }

    public static Trigger callback() {
        return new Trigger() {
            @Override
            public boolean match(Update update) {
                return update.hasCallbackQuery();
            }
        };
    }

}
