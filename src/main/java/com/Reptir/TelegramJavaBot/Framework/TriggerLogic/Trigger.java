package com.Reptir.TelegramJavaBot.Framework.TriggerLogic;

public interface Trigger<U> {
    boolean match(U update);
}
