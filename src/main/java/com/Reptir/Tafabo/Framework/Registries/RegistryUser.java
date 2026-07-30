package com.Reptir.Tafabo.Framework.Registries;

import com.Reptir.Tafabo.Framework.AdapterLogic.BotUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

// регистр состояний пользователя
public class RegistryUser {
    private final Logger logger = LoggerFactory.getLogger(RegistryUser.class);
    private final Set<BotUser> users = new HashSet<>();

    public Set<BotUser> getUsers() {
        return users;
    }

    public void addBotUserIfNotRegistered(BotUser botUser) {
        users.add(botUser);
    }
}
