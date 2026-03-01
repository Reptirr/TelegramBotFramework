package com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryDialogState;

import java.time.Duration;
import java.time.Instant;

public class TimeoutService {
    private long timeoutSeconds;
    private final RegistryDialogState registryDialogState;

    public TimeoutService(RegistryDialogState registryDialogState, long timeoutSeconds) {
        this.registryDialogState = registryDialogState;
        this.timeoutSeconds = timeoutSeconds;
    }

    public void setTimeout(short timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    void checkTimeout() { // проверяет регистр диалогов на наличие таймаутов
        for (UserDialogState state : registryDialogState.getMap().values()) {
            Instant now = Instant.now(); // время прямо сейчас
            Instant lastAction = Instant.ofEpochSecond(state.timeLastAction); // время последнего действия с диалогом

            Duration timeDelta = Duration.between(lastAction, now); // разность

            if (timeDelta.toSeconds() > timeoutSeconds) {  // логика таймаута
                registryDialogState.remove(state.userId);
            }
        }
    }
}
