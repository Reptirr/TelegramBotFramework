package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record Context(Messenger messenger, Update update, DialogManager dialogManager, RegistryUser registryUser) {
    public CommandTrigger trigger() {
        if (update.hasMessage()) {
            return CommandTrigger.USER_INPUT;
        }
        if (update.hasCallbackQuery()) {
            return CommandTrigger.CALLBACK;
        }
        if (update.hasEditedMessage()) {
            return CommandTrigger.MESSAGE_EDITED;
        }
        return CommandTrigger.UNKNOWN;
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
}
