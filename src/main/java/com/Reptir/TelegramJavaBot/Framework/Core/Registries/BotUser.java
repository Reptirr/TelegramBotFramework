package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import org.telegram.telegrambots.meta.api.objects.User;

public class BotUser {
    private final org.telegram.telegrambots.meta.api.objects.User user;
    private String handleCommandName = "EmptyCommand";

    public long getId() {
        return user.getId();
    }

    public BotUser(User user) {
        this.user = user;
    }

    public String getHandleCommandName() {
        return handleCommandName;
    }

    public void setHandleCommandName(String handleCommandName) {
        this.handleCommandName = handleCommandName;
    }
}
