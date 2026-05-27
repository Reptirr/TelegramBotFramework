package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.ChatType;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.UpdateType;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record Context(Messenger messenger, Update update, DialogManager dialogManager, RegistryUser registryUser) {
    public UpdateType updateType() {
        if (update.hasMessage()) {
            return UpdateType.USER_INPUT;
        }
        if (update.hasCallbackQuery()) {
            return UpdateType.CALLBACK;
        }
        if (update.hasEditedMessage()) {
            return UpdateType.MESSAGE_EDITED;
        }
        return UpdateType.UNKNOWN;
    }

    public long chatId() {
        if (update.hasMessage()) {
            return update.getMessage().getChat().getId();
        } else if (update.hasEditedMessage()) {
            return update.getEditedMessage().getChat().getId();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }

        throw new IllegalStateException("Update does not contain chat id");
    }

    public User user() {
        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        } else if (update.hasEditedMessage()) {
            return update.getEditedMessage().getFrom();
        } else if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        }

        throw new IllegalStateException("Update does not contain user");
    }

    public Chat chat() {
        if (update.hasMessage()) {
            return update.getMessage().getChat();
        }

        if (update.hasEditedMessage()) {
            return update.getEditedMessage().getChat();
        }

        if (update.hasCallbackQuery() && update.getCallbackQuery().getMessage() != null) {
            return update.getCallbackQuery().getMessage().getChat();
        }

        throw new IllegalStateException("Update does not contain chat");
    }

    public Message message() {
        if (update.hasMessage()) {
            return update.getMessage();
        } else if (update.hasEditedMessage()) {
            return update.getEditedMessage();
        } else if (update.hasCallbackQuery()) {
            return (Message) update.getCallbackQuery().getMessage();
        }

        throw new IllegalStateException("Update does not contain message");
    }

    public ChatType chatType() {
        Chat chat = chat();
        if (chat == null) return null;

        return ChatType.map(chat.getType());
    }


}
