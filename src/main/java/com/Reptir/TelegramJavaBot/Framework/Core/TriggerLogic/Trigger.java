package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic.Context;

public interface Trigger {
    boolean match(Context ctx);
}
