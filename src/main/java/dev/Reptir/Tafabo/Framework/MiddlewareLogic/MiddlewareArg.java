package dev.Reptir.Tafabo.Framework.MiddlewareLogic;

import dev.Reptir.Tafabo.Framework.dto.Context;

public record MiddlewareArg<R, U, M>(
        R arg,
        Context<U, M> context
) {}