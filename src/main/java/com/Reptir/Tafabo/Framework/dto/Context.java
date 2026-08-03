package com.Reptir.Tafabo.Framework.dto;

public record Context<U, M>(
        U update,
        M messenger
) {

}