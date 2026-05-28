package com.Reptir.TelegramJavaBot.Framework.Core.Registries;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.BotUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// регистр состояний пользователя
public class RegistryUser {
    private final Logger logger = LoggerFactory.getLogger(RegistryUser.class);
    private final Map<Long, BotUser> users = new ConcurrentHashMap<>();

    public BotUser getBotUser(Long id) {
        if (users.containsKey(id)) return users.get(id);
        return null;
    }

    public Map<Long, BotUser> getUsers() {
        return users;
    }

    public void addBotUserIfNotRegistered(BotUser botUser) {
        if (!users.containsKey(botUser.getId())) {
            logger.info("Registered new BotUser '@{}' with id '{}'", botUser.getUser().getUserName(), botUser.getId());
            users.put(botUser.getId(), botUser);
        }
    }

    public void removeBotUser(Long id) {
        users.remove(id);
    }
}
