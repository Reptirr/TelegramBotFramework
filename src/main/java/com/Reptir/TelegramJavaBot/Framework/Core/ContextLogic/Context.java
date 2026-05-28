package com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record Context(TelegramContext tgContext, DialogManager dialogManager, RegistryUser registryUser) {


    public UpdateType updateType() {
        if (tgContext.update().hasMessage()) {
            return UpdateType.USER_INPUT;
        }
        if (tgContext.update().hasCallbackQuery()) {
            return UpdateType.CALLBACK;
        }
        if (tgContext.update().hasEditedMessage()) {
            return UpdateType.MESSAGE_EDITED;
        }
        return UpdateType.UNKNOWN;
    }
    public ChatType chatType() {
        Chat chat = chat();
        if (chat == null) return null;

        return ChatType.map(chat.getType());
    }

    public long chatId() {
        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage().getChat().getId();
        } else if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage().getChat().getId();
        } else if (tgContext.update().hasCallbackQuery()) {
            return tgContext.update().getCallbackQuery().getMessage().getChatId();
        }

        throw new IllegalStateException("Update does not contain chat id");
    }
    public Chat chat() {
        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage().getChat();
        }

        if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage().getChat();
        }

        if (tgContext.update().hasCallbackQuery() && tgContext.update().getCallbackQuery().getMessage() != null) {
            return tgContext.update().getCallbackQuery().getMessage().getChat();
        }

        throw new IllegalStateException("Update does not contain chat");
    }
    public User user() {
        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage().getFrom();
        } else if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage().getFrom();
        } else if (tgContext.update().hasCallbackQuery()) {
            return tgContext.update().getCallbackQuery().getFrom();
        }

        throw new IllegalStateException("Update does not contain user");
    }
    public Message message() {
        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage();
        } else if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage();
        } else if (tgContext.update().hasCallbackQuery()) {
            return (Message) tgContext.update().getCallbackQuery().getMessage();
        }

        throw new IllegalStateException("Update does not contain message");
    }
    public CallbackQuery callbackQuery() {
        if (tgContext.update().hasCallbackQuery()) {
            return tgContext.update().getCallbackQuery();
        }

        throw new IllegalStateException("Update does not contain callbackQuery");
    }

}
