package com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Messenger;
import org.telegram.telegrambots.meta.api.objects.Update;

public record TelegramContext(Update update, Messenger messenger) {

}
