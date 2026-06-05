package com.Reptir.TelegramJavaBot.Framework.TriggerLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;

public interface Trigger {
    boolean match(Context ctx);
}
