package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.telegram.telegrambots.meta.api.objects.Update;

public record Context(Messenger messenger, Update update, DialogManager dialogManager, RegistryUser registryUser) {
    public CommandTrigger trigger() {
        if (update.hasMessage()) {
            return CommandTrigger.USER_INPUT;
        }
        if (update.hasCallbackQuery()) {
            return CommandTrigger.CALLBACK;
        }
        if (update.hasEditedMessage()) {
            return CommandTrigger.MESSAGE_EDITED;
        }
        return CommandTrigger.UNKNOWN;
    }
}
