package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;
import org.telegram.telegrambots.meta.api.objects.User;

public class BotUser {
    private final org.telegram.telegrambots.meta.api.objects.User user;
    private UserDialogState dialogState; // null если диалога нет

    public long getId() {
        return user.getId();
    }
    public UserDialogState getDialogState() {
        return dialogState;
    }
    public void setDialogState(UserDialogState dialogState) {
        this.dialogState = dialogState;
    }


    public BotUser(User user) {
        this.user = user;
        this.dialogState = null;
    }


}
