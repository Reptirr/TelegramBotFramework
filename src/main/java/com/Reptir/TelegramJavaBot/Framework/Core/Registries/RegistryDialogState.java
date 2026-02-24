package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.BaseDialog;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// хранит действующие состояния диалогов, если диалог заканчивается то он, удаляется из регистра для анонимности
public class RegistryDialogState {
    private final Map<Long, UserDialogState> userStates = new ConcurrentHashMap<>();

    public boolean isDialogExists(Long userId) {
        return userStates.containsKey(userId);
    }

    public UserDialogState get(Long userId) {
        if (userStates.containsKey(userId)) {
            return userStates.get(userId);
        }
        return null;
    }

    public void remove(Long userId) {
        userStates.remove(userId);
    }

    public void createDialog(Long userId, BaseDialog dialog) {   // вызывается из BaseCommand
        userStates.put(userId, new UserDialogState(dialog, userId));
    }

}
