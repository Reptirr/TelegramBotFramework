package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

public interface BaseCommand {
    void execute(Context ctx, String[] args);

    default boolean isForUserInput() {
        return true;
    }
    default void executeAlert(Context ctx) {
        ctx.getMessenger().answerCallback(ctx.getCallback().getId(), "", false);
    }
}
