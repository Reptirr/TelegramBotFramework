package com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryDialogState;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class DialogManager {
    RegistryDialogState registryDialogState;
    TelegramClient tgClient;

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
            boolean isFinished = state.dialog.nextStep(ctx.getMessage().getChatId(), ctx, ctx.getMessage().getText(), registryDialogState);

            if (isFinished) {
                killDialog(userId);
            }

            return !isFinished;
        } else {
            return false;
        }
    }

    private void killDialog(Long userId) {
        registryDialogState.remove(userId);
    }

    public void startDialog(BaseDialog dialog, Long userId, Context ctx) {
        registryDialogState.createDialog(userId, dialog);
        executeDialogIfExists(ctx);
    }
}
