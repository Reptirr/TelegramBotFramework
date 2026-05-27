package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic;

import org.telegram.telegrambots.meta.api.objects.Update;

public class Triggers {
    public static Trigger messageStartsWith(String text) {
        return new Trigger() {
            @Override
            public boolean match(Update update) {
                if (!update.hasMessage()) return false;
                return update.getMessage().getText().startsWith(text);
            }
        };
    }

    public static Trigger messageFirstWordEquals(String text) {
        return new Trigger() {
            @Override
            public boolean match(Update update) {
                if (!update.hasMessage()) return false;
                return update.getMessage().getText().split(" ")[0].equals(text);
            }
        };
    }

}
