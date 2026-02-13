package com.Reptir.TelegramJavaBot.CommandLogic;

import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class InformationForCommand {
    private Message message;
    private TelegramClient tgClient;

    public InformationForCommand(Message message, TelegramClient tgClient) {
        this.message = message;
        this.tgClient = tgClient;
    }

    public Message getMessage() {
        return message;
    }
    public TelegramClient getTgClient() {
        return tgClient;
    }
}
