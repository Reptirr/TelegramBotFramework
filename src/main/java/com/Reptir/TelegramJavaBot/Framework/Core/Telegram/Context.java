package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Context {
    private final Message message;
    private final TelegramClient tgClient;
    private final CallbackQuery callback;
    private final DialogManager dialogManager;
    private final RegistryUser registryUser;

    public Context(Message message, TelegramClient tgClient, CallbackQuery callback, DialogManager dialogManager, RegistryUser registryUser) {
        this.message = message;
        this.tgClient = tgClient;
        this.callback = callback;
        this.dialogManager = dialogManager;
        this.registryUser = registryUser;
    }

    public Message getMessage() {
        return message;
    }
    public TelegramClient getTgClient() {
        return tgClient;
    }
    public CallbackQuery getCallback() {
        return callback;
    }
    public DialogManager getDialogManager() {
        return dialogManager;
    }
    public RegistryUser getRegistryUser() {
        return registryUser;
    }

    public void edit(String text, InlineKeyboardMarkup markup) {
        if (message.getFrom() != null && message.getFrom().getIsBot()) throw new RuntimeException("Cant use ctx.edit() in non-bot messages");
        EditMessageText editMessageText;
        if (markup != null) {
            editMessageText = TelegramWrappers.editMessage(message.getChatId(), message.getMessageId(), text, markup);
        } else {
            editMessageText = TelegramWrappers.editMessage(message.getChatId(), message.getMessageId(), text);
        }

        try {
            tgClient.execute(editMessageText);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
            // logging in future
        }
    }
}
