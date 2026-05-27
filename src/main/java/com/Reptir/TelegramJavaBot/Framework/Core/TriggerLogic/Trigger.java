package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface Trigger {
    boolean match(Update update);
}
