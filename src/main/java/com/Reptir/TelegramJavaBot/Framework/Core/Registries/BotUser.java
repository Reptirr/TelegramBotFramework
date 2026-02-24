package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import org.telegram.telegrambots.meta.api.objects.User;

public class BotUser {
    private final org.telegram.telegrambots.meta.api.objects.User user;

    public long getId() {
        return user.getId();
    }

    public BotUser(User user) {
        this.user = user;
    }


}
