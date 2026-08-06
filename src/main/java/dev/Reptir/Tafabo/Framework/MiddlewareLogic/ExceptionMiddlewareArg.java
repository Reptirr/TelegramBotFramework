package dev.Reptir.Tafabo.Framework.MiddlewareLogic;

import dev.Reptir.Tafabo.Framework.dto.Context;

public record ExceptionMiddlewareArg<U, M> (
        Throwable exception,
        PipelineStage stage,
        Context<U, M> ctx
) {

}
