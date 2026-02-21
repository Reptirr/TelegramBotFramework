package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class TelegramWrappers {

    // ====================== SEND MESSAGE ======================
    public static SendMessage sendMessage(Long chatId, String text) {
        return sendMessage(String.valueOf(chatId), text);
    }

    public static SendMessage sendMessage(String chatId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }

    public static SendMessage sendMessage(Long chatId, String text, InlineKeyboardMarkup markup) {
        return sendMessage(String.valueOf(chatId), text, markup);
    }

    public static SendMessage sendMessage(String chatId, String text, InlineKeyboardMarkup markup) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(markup)
                .build();
    }

    public static SendMessage replyMessage(Long chatId, Integer replyToMessageId, String text) {
        return replyMessage(String.valueOf(chatId), replyToMessageId, text);
    }

    public static SendMessage replyMessage(String chatId, Integer replyToMessageId, String text) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(replyToMessageId)
                .text(text)
                .build();
    }

    public static SendMessage replyMessage(Long chatId, Integer replyToMessageId, String text, InlineKeyboardMarkup markup) {
        return replyMessage(String.valueOf(chatId), replyToMessageId, text, markup);
    }

    public static SendMessage replyMessage(String chatId, Integer replyToMessageId, String text, InlineKeyboardMarkup markup) {
        return SendMessage.builder()
                .chatId(chatId)
                .replyToMessageId(replyToMessageId)
                .text(text)
                .replyMarkup(markup)
                .build();
    }

    // ====================== EDIT MESSAGE ======================
    public static EditMessageText editMessage(Long chatId, Integer messageId, String text) {
        return editMessage(String.valueOf(chatId), messageId, text);
    }

    public static EditMessageText editMessage(String chatId, Integer messageId, String text) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .build();
    }

    public static EditMessageText editMessage(Long chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        return editMessage(String.valueOf(chatId), messageId, text, markup);
    }

    public static EditMessageText editMessage(String chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        return EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(markup)
                .build();
    }

    // ====================== DELETE MESSAGE ======================
    public static DeleteMessage deleteMessage(Long chatId, Integer messageId) {
        return deleteMessage(String.valueOf(chatId), messageId);
    }

    public static DeleteMessage deleteMessage(String chatId, Integer messageId) {
        return DeleteMessage.builder()
                .chatId(chatId)
                .messageId(messageId)
                .build();
    }

    // ====================== INLINE BUTTONS ======================
    public static InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    public static InlineKeyboardButton buttonUrl(String text, String url) {
        return InlineKeyboardButton.builder()
                .text(text)
                .url(url)
                .build();
    }

    public static InlineKeyboardButton buttonSwitchInline(String text, String query) {
        return InlineKeyboardButton.builder()
                .text(text)
                .switchInlineQuery(query)
                .build();
    }

    public static InlineKeyboardButton buttonSwitchInlineCurrent(String text, String query) {
        return InlineKeyboardButton.builder()
                .text(text)
                .switchInlineQueryCurrentChat(query)
                .build();
    }
}