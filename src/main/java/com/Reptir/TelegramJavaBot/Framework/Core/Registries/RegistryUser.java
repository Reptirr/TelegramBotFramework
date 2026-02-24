package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// регистр состояний пользователя
public class RegistryUser {
    private final Map<Long, BotUser> users = new ConcurrentHashMap<>();

    public BotUser getBotUser(Long id) {
        if (users.containsKey(id)) return users.get(id);
        return null;
    }

    public Map<Long, BotUser> getUsers() {
        return users;
    }

    public void addBotUserIfNotInRegistry(BotUser botUser) {
        if (!users.containsKey(botUser.getId())) {
            users.put(botUser.getId(), botUser);
        }
    }

    public void removeBotUser(Long id) {
        users.remove(id);
    }
}
