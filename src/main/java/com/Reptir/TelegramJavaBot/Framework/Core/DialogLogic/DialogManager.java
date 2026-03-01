package com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryDialogState;


public class DialogManager {
    RegistryDialogState registryDialogState;

    public DialogManager(RegistryDialogState registryDialogState) {
        this.registryDialogState = registryDialogState;
    }

    /**
    * @return true, если диалог обрабатывается, false если диалога нет
    */
    public boolean executeDialogIfExists(Context ctx) {
        Long userId = ctx.getMessage().getFrom().getId();
        if (registryDialogState.isDialogExists(userId)) {
            UserDialogState state = registryDialogState.get(userId);
            state.timeLastAction = Long.valueOf(ctx.getMessage().getDate());
            boolean isFinished = state.dialog.nextStep(ctx.getMessage().getFrom().getId(), ctx, ctx.getMessage().getText(), registryDialogState);

            if (isFinished) {
                killDialog(userId);
            }
            return true;
        }
        return false;
    }

    private void killDialog(Long userId) {
        registryDialogState.remove(userId);
    }

    public void startDialog(BaseDialog dialog, Long userId, Context ctx) {
        registryDialogState.createDialog(userId, dialog, Long.valueOf(ctx.getMessage().getDate()));
        executeDialogIfExists(ctx);
    }
}
