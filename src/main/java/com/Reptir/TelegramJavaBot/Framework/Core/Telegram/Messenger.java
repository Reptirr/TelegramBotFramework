package com.Reptir.TelegramJavaBot.Framework.Core.Telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Messenger {

    private static final Logger log = LoggerFactory.getLogger(Messenger.class);

    private final TelegramClient tgClient;

    public Messenger(TelegramClient tgClient) {
        this.tgClient = tgClient;
    }

    public void answerCallback(String callbackQueryId, String text, boolean showAlert) {
        try {
            tgClient.execute(
                    AnswerCallbackQuery.builder()
                            .callbackQueryId(callbackQueryId)
                            .text(text)
                            .showAlert(showAlert)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to answer callbackQueryId={}", callbackQueryId, e);
        }
    }

    // ====================== TEXT ======================

    public void sendText(Long chatId, String text) {
        try {
            tgClient.execute(
                    SendMessage.builder()
                            .chatId(String.valueOf(chatId))
                            .text(text)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to send text message to chatId={}", chatId, e);
        }
    }

    public void sendText(Long chatId, String text, InlineKeyboardMarkup markup) {
        try {
            tgClient.execute(
                    SendMessage.builder()
                            .chatId(String.valueOf(chatId))
                            .text(text)
                            .replyMarkup(markup)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to send text message with markup to chatId={}", chatId, e);
        }
    }

    public void replyText(Long chatId, Integer messageId, String text) {
        try {
            tgClient.execute(
                    SendMessage.builder()
                            .chatId(String.valueOf(chatId))
                            .replyToMessageId(messageId)
                            .text(text)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to reply text message to chatId={}, messageId={}", chatId, messageId, e);
        }
    }

    public void replyText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        try {
            tgClient.execute(
                    SendMessage.builder()
                            .chatId(String.valueOf(chatId))
                            .replyToMessageId(messageId)
                            .text(text)
                            .replyMarkup(markup)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to reply text message with markup to chatId={}, messageId={}", chatId, messageId, e);
        }
    }

    // ====================== EDIT ======================

    public void editText(Long chatId, Integer messageId, String text) {
        try {
            tgClient.execute(
                    EditMessageText.builder()
                            .chatId(String.valueOf(chatId))
                            .messageId(messageId)
                            .text(text)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to edit message text chatId={}, messageId={}", chatId, messageId, e);
        }
    }

    public void editText(Long chatId, Integer messageId, String text, InlineKeyboardMarkup markup) {
        try {
            tgClient.execute(
                    EditMessageText.builder()
                            .chatId(String.valueOf(chatId))
                            .messageId(messageId)
                            .text(text)
                            .replyMarkup(markup)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to edit message text with markup chatId={}, messageId={}", chatId, messageId, e);
        }
    }

    // ====================== DELETE ======================

    public void deleteMessage(Long chatId, Integer messageId) {
        try {
            tgClient.execute(
                    DeleteMessage.builder()
                            .chatId(String.valueOf(chatId))
                            .messageId(messageId)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to delete message chatId={}, messageId={}", chatId, messageId, e);
        }
    }

    // ====================== PHOTO ======================

    public void sendPhoto(Long chatId, String photoPath) {
        sendPhoto(chatId, photoPath, null, null);
    }

    public void sendPhoto(Long chatId, String photoPath, String caption) {
        sendPhoto(chatId, photoPath, caption, null);
    }

    public void sendPhoto(Long chatId, String photoPath, String caption, InlineKeyboardMarkup markup) {
        try {
            SendPhoto.SendPhotoBuilder builder = SendPhoto.builder()
                    .chatId(String.valueOf(chatId))
                    .photo(new InputFile(photoPath));

            if (caption != null) {
                builder.caption(caption);
            }

            if (markup != null) {
                builder.replyMarkup(markup);
            }

            tgClient.execute(builder.build());

        } catch (Exception e) {
            log.error("Failed to send photo chatId={}", chatId, e);
        }
    }

    // ====================== BUTTONS ======================

    public InlineKeyboardButton button(String text, String callbackData) {
        return InlineKeyboardButton.builder()
                .text(text)
                .callbackData(callbackData)
                .build();
    }

    public InlineKeyboardButton buttonUrl(String text, String url) {
        return InlineKeyboardButton.builder()
                .text(text)
                .url(url)
                .build();
    }

    public InlineKeyboardButton buttonSwitchInline(String text, String query) {
        return InlineKeyboardButton.builder()
                .text(text)
                .switchInlineQuery(query)
                .build();
    }

    public InlineKeyboardButton buttonSwitchInlineCurrent(String text, String query) {
        return InlineKeyboardButton.builder()
                .text(text)
                .switchInlineQueryCurrentChat(query)
                .build();
    }
}