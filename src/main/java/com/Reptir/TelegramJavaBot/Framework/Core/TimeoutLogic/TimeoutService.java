package com.Reptir.TelegramJavaBot.Framework.Core.TimeoutLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryDialogState;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;

public class TimeoutService {
    private final short timeoutSeconds = 10;
    private final RegistryDialogState registryDialogState;

    public TimeoutService(RegistryDialogState registryDialogState) {
        this.registryDialogState = registryDialogState;
    }

    void checkTimeout() { // проверяет регистр диалогов на наличие таймаутов
        
    }
}
