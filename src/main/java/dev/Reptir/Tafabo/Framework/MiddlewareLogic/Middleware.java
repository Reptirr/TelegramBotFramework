package dev.Reptir.Tafabo.Framework.MiddlewareLogic;

import java.util.function.Function;

public record Middleware<T, R>(
        int priority,
        Function<T, R> middleware
) {

}
