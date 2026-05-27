package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface Trigger {
    boolean match(Context ctx);
}
