package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Context {
    private final Update update;
    private final Messenger messenger;
    private final DialogManager dialogManager;
    private final RegistryUser registryUser;

    public Context(TelegramClient tgClient, Update update, DialogManager dialogManager, RegistryUser registryUser) {
        this.update = update;
        this.messenger = new Messenger(tgClient);
        this.dialogManager = dialogManager;
        this.registryUser = registryUser;
    }

    public Messenger getMessenger() {
        return messenger;
    }
    public Update getUpdate() {
        return update;
    }
    public DialogManager getDialogManager() {
        return dialogManager;
    }
    public RegistryUser getRegistryUser() {
        return registryUser;
    }
}
