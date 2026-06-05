package com.Reptir.TelegramJavaBot.Framework.ContextLogic;

import com.Reptir.TelegramJavaBot.Framework.Telegram.Messenger;
import org.telegram.telegrambots.meta.api.objects.Update;

public record TelegramContext(Update update, Messenger messenger) {

}
