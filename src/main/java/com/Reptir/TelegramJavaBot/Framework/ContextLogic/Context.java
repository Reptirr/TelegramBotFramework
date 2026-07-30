package com.Reptir.TelegramJavaBot.Framework.ContextLogic;

import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;

public record Context<U, M>(
        U update,
        M messenger,
        RegistryUser registryUser
) {

}