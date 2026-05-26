package com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;

import java.time.Duration;
import java.time.Instant;

public class TimeoutService {
    private long timeoutSeconds;
    private final RegistryUser registryUser;

    public TimeoutService(long timeoutSeconds, RegistryUser registryUser) {
        this.registryUser = registryUser;
        this.timeoutSeconds = timeoutSeconds;
    }

    public void setTimeout(short timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public void checkTimeout() { // проверяет регистр диалогов на наличие таймаутов
        for (BotUser user : registryUser.getUsers().values()) {
            UserDialogState state = user.getDialogState();
            if (state == null) continue;

            Instant now = Instant.now(); // время прямо сейчас
            Instant lastAction = Instant.ofEpochMilli(state.timeLastAction); // время последнего действия с диалогом

            Duration timeDelta = Duration.between(lastAction, now); // разность

            if (timeDelta.toSeconds() > timeoutSeconds) {  // логика таймаута
                user.setDialogState(null);
            }
        }
    }
}
