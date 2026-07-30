package com.Reptir.TelegramJavaBot.Framework.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;

public interface BaseCommand<U, M> {
    void execute(Context<U, M> ctx);
}
