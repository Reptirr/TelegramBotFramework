package com.Reptir.TelegramJavaBot.Framework.TriggerLogic;

public class Triggers {
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
}