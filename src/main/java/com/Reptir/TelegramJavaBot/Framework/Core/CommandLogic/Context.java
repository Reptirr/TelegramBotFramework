package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.TelegramWrappers;
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

    public Context(Message message, TelegramClient tgClient, CallbackQuery callback) {
        this.message = message;
        this.tgClient = tgClient;
        this.callback = callback;
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

    public void edit(String text, InlineKeyboardMarkup markup) {
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
