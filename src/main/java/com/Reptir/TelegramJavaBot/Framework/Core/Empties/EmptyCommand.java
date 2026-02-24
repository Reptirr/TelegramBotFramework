package com.Reptir.TelegramJavaBot.Framework.Core.Empties;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;

public class EmptyCommand implements BaseCommand {

    @Override
    public String getName() {
        return "callbackNotForHandle";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(Context ctx, String[] args) {

    }
}
