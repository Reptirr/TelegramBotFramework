package com.Reptir.TelegramJavaBot.Realizations.CommandsRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.TelegramWrappers;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class JustButton implements BaseCommand {
    @Override
    public String getName() {
        return "nigger";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(Context ctx, String[] args) {
        ctx.edit("yes im nigger bra", null);
    }
}
