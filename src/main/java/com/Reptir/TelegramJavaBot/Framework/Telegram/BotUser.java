package com.Reptir.TelegramJavaBot.Framework.Telegram;

import org.telegram.telegrambots.meta.api.objects.User;

public class BotUser {
    private final org.telegram.telegrambots.meta.api.objects.User user;

    public long getId() {
        return user.getId();
    }
    public User getUser() {
        return user;
    }


    public BotUser(User user) {
        this.user = user;
    }
}
