package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic.Context;

public interface BaseCommand {
    void execute(Context ctx);
}
