package com.Reptir.TelegramJavaBot.Framework.ThreadLogic;

import java.util.UUID;

public record ThreadId(UUID id) {
    public ThreadId() {
        this(UUID.randomUUID());
    }
}
