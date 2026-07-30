package com.Reptir.Tafabo.Framework.CommandLogic;

import com.Reptir.Tafabo.Framework.ContextLogic.Context;

public interface BaseCommand<U, M> {
    void execute(Context<U, M> ctx);
}
