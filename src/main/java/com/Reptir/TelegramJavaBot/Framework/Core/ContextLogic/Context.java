package com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chat.Chat;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public record Context(
        TelegramContext tgContext,
        DialogManager dialogManager,
        RegistryUser registryUser
) {

    public UpdateType updateType() {
        if (tgContext == null || tgContext.update() == null) {
            return UpdateType.UNKNOWN;
        }

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

        if (chat == null) {
            return null;
        }

        return ChatType.map(chat.getType());
    }

    public Long chatId() {
        if (tgContext == null || tgContext.update() == null) {
            return null;
        }

        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage().getChat().getId();
        }

        if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage().getChat().getId();
        }

        if (tgContext.update().hasCallbackQuery()
                && tgContext.update().getCallbackQuery().getMessage() != null) {

            return tgContext.update()
                    .getCallbackQuery()
                    .getMessage()
                    .getChatId();
        }

        return null;
    }

    public Chat chat() {
        if (tgContext == null || tgContext.update() == null) {
            return null;
        }

        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage().getChat();
        }

        if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage().getChat();
        }

        if (tgContext.update().hasCallbackQuery()
                && tgContext.update().getCallbackQuery().getMessage() != null) {

            return tgContext.update()
                    .getCallbackQuery()
                    .getMessage()
                    .getChat();
        }

        return null;
    }

    public User user() {
        if (tgContext == null || tgContext.update() == null) {
            return null;
        }

        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage().getFrom();
        }

        if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage().getFrom();
        }

        if (tgContext.update().hasCallbackQuery()) {
            return tgContext.update().getCallbackQuery().getFrom();
        }

        return null;
    }

    public Message message() {
        if (tgContext == null || tgContext.update() == null) {
            return null;
        }

        if (tgContext.update().hasMessage()) {
            return tgContext.update().getMessage();
        }

        if (tgContext.update().hasEditedMessage()) {
            return tgContext.update().getEditedMessage();
        }

        if (tgContext.update().hasCallbackQuery()
                && tgContext.update().getCallbackQuery().getMessage() != null) {

            return (Message) tgContext.update()
                    .getCallbackQuery()
                    .getMessage();
        }

        return null;
    }

    public CallbackQuery callbackQuery() {
        if (tgContext == null || tgContext.update() == null) {
            return null;
        }

        if (tgContext.update().hasCallbackQuery()) {
            return tgContext.update().getCallbackQuery();
        }

        return null;
    }
}