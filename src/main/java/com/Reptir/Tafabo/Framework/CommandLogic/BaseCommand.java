package com.Reptir.Tafabo.Framework.CommandLogic;

import com.Reptir.Tafabo.Framework.dto.Context;

public interface BaseCommand<U, M> {
    void execute(Context<U, M> ctx);
}
