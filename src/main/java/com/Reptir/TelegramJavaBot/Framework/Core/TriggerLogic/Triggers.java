package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.ChatType;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.UpdateType;

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
        return new Trigger() {
            @Override
            public boolean match(Context ctx) {
                return  ctx.message() != null
                        && ctx.message().hasText()
                        && ctx.message().getText().startsWith(text);
            }
        };
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

    // =======
    // Type work
    // =======
    public static Trigger chatType(ChatType type) {
        return ctx -> ctx.chatType() == type;
    }
    public static Trigger updateType(UpdateType type) {
        return ctx -> ctx.updateType() == type;
    }
}
