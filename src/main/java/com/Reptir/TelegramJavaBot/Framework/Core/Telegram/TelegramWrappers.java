package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramWrappers {

    // ====================== SEND MESSAGE ======================

    // Только текст
    public static SendMessage sendMessage(Long chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    // С маркапом
    public static SendMessage sendMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();
    }

    // По ответу на сообщение (replyToMessageId)
    public static SendMessage replyMessage(Long chatId, Integer replyToMessageId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyToMessageId(replyToMessageId)
                .build();
    }

    public static SendMessage replyMessage(Long chatId, Integer replyToMessageId, String text, InlineKeyboardMarkup markup) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyToMessageId(replyToMessageId)
                .replyMarkup(markup)
                .build();
    }

    // ====================== EDIT MESSAGE ======================

    public static EditMessageText editMessage(Long chatId, Integer messageId, String text) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .build();
    }

    public static EditMessageText editMessage(Long chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(markup)
                .build();
    }

    // ====================== DELETE MESSAGE ======================

    public static DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return DeleteMessage.builder()
                        .chatId(chatId.toString())
                        .messageId(messageId)
                        .build();
    }

    // ====================== ПРОСТАЯ КНОПКА С CALLBACK ======================
    public static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    // ====================== КНОПКА С URL ======================
    public static InlineKeyboardButton buttonUrl(String text, String url) {
        return InlineKeyboardButton.builder()
                .text(text)
                .url(url)
                .build();
    }

    // ====================== КНОПКА ДЛЯ SWITCH INLINE QUERY ======================
    public static InlineKeyboardButton buttonSwitchInline(String text, String query) {
        return InlineKeyboardButton.builder()
                .text(text)
                .switchInlineQuery(query)
                .build();
    }

    // ====================== КНОПКА ДЛЯ SWITCH INLINE QUERY CURRENT CHAT ======================
    public static InlineKeyboardButton buttonSwitchInlineCurrent(String text, String query) {
        return InlineKeyboardButton.builder()
                .text(text)
                .switchInlineQueryCurrentChat(query)
                .build();
    }
}
