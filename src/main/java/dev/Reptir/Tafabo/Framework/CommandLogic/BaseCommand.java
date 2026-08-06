package dev.Reptir.Tafabo.Framework.CommandLogic;

import dev.Reptir.Tafabo.Framework.dto.Context;

public interface BaseCommand<U, M> {
    void execute(Context<U, M> ctx);
}
