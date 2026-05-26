package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class Context { // !
    private final Message message;
    private final Messenger messenger;
    private final CallbackQuery callback;
    private final DialogManager dialogManager;
    private final RegistryUser registryUser;

    public Context(Message message, TelegramClient tgClient, CallbackQuery callback, DialogManager dialogManager, RegistryUser registryUser) {
        this.message = message;
        this.messenger = new Messenger(tgClient);
        this.callback = callback;
        this.dialogManager = dialogManager;
        this.registryUser = registryUser;
    }

    public Message getMessage() {
        return message;
    }
    public Messenger getMessenger() {
        return messenger;
    }
    public CallbackQuery getCallback() {
        return callback;
    }
    public DialogManager getDialogManager() {
        return dialogManager;
    }
    public RegistryUser getRegistryUser() {
        return registryUser;
    }

    public void edit(String text, InlineKeyboardMarkup markup) {
        if (message.getFrom() != null && message.getFrom().getIsBot()) throw new RuntimeException("Cant use ctx.edit() in non-bot messages");

        if (markup != null) {
            messenger.editText(message.getChatId(), message.getMessageId(), text, markup);
        } else {
            messenger.editText(message.getChatId(), message.getMessageId(), text);
        }
    }
}
