package com.Reptir.TelegramJavaBot.Framework.TriggerLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.ChatType;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.UpdateType;

public class Triggers {

    // =========
    // Вентили
    // =========
    public static Trigger and(Trigger... triggers) {
        return ctx -> {
            for (Trigger trigger : triggers) {
                if (!trigger.match(ctx)) return false;
            }
            return true;
        };
    }

    public static Trigger or(Trigger... triggers) {
        return ctx -> {
            for (Trigger trigger : triggers) {
                if (trigger.match(ctx)) return true;
            }
            return false;
        };
    }

    // ==========
    // Text work
    // ==========
    public static Trigger textStartsWith(String text) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().startsWith(text);
    }

    public static Trigger textEquals(String text) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().equals(text);
    }

    public static Trigger textContains(String text) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().contains(text);
    }

    public static Trigger textMatches(String regex) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().matches(regex);
    }

    public static Trigger hasText() {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText();
    }

    // ==========
    // Type work
    // ==========
    public static Trigger chatType(ChatType type) {
        return ctx -> ctx.chatType() == type;
    }

    public static Trigger updateType(UpdateType type) {
        return ctx -> ctx.updateType() == type;
    }

    // =========
    // Message work
    // =========
    public static Trigger hasMessage() {
        return ctx -> ctx.message() != null;
    }

    public static Trigger messageFromUser(long userId) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().getFrom() != null
                        && ctx.message().getFrom().getId() == userId;
    }

    public static Trigger replyToMessage() {
        return ctx ->
                ctx.message() != null
                        && ctx.message().isReply();
    }

    // =========
    // Callback work
    // =========
    public static Trigger callbackDataEquals(String data) {
        return ctx ->
                ctx.callbackQuery() != null
                        && ctx.callbackQuery().getData() != null
                        && ctx.callbackQuery().getData().equals(data);
    }

    public static Trigger callbackDataStartsWith(String prefix) {
        return ctx ->
                ctx.callbackQuery() != null
                        && ctx.callbackQuery().getData() != null
                        && ctx.callbackQuery().getData().startsWith(prefix);
    }

    public static Trigger hasCallback() {
        return ctx -> ctx.callbackQuery() != null;
    }

    // ==========
    // Message context helpers
    // ==========
    public static Trigger textLengthGreaterThan(int len) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().length() > len;
    }

    public static Trigger textLengthLessThan(int len) {
        return ctx ->
                ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().length() < len;
    }

    public static Trigger textIn(String... values) {
        return ctx -> {
            if (ctx.message() == null || !ctx.message().hasText()) return false;

            String t = ctx.message().getText();
            for (String v : values) {
                if (t.equals(v)) return true;
            }
            return false;
        };
    }

    // ==========
    // Combined helpers
    // ==========
    public static Trigger messageTextEquals(String text) {
        return and(hasText(), textEquals(text));
    }

    public static Trigger command(String cmd) {
        return ctx -> {
            if (ctx.message() == null || !ctx.message().hasText()) {
                return false;
            }

            String text = ctx.message().getText();

            return text.startsWith(cmd)
                    && (text.length() == cmd.length()
                    || text.charAt(cmd.length()) == ' ');
        };
    }

    public static Trigger privateChat() {
        return chatType(ChatType.PRIVATE);
    }

    public static Trigger groupChat() {
        return chatType(ChatType.GROUP);
    }

    public static Trigger channelChat() {
        return chatType(ChatType.CHANNEL);
    }
}