package com.Reptir.Tafabo.Framework.dto;

import com.Reptir.Tafabo.Framework.Registries.RegistryUser;

public record Context<U, M>(
        U update,
        M messenger,
        RegistryUser registryUser
) {

}