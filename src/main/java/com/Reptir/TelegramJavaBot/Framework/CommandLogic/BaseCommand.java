package com.Reptir.TelegramJavaBot.Framework.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;

public interface BaseCommand {
    void execute(Context ctx);
}
