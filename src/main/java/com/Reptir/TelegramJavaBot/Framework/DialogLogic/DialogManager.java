package com.Reptir.TelegramJavaBot.Framework.DialogLogic;

import com.Reptir.TelegramJavaBot.Framework.Telegram.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DialogManager {
    private final Logger logger = LoggerFactory.getLogger(DialogManager.class);

    RegistryUser registryUser;

    public DialogManager(RegistryUser registryUser) {
        this.registryUser = registryUser;
    }


    /**
     * @return true, если диалог обрабатывается, false если диалога нет
     */
    public boolean executeDialogIfExists(long id, Context ctx) {
        BotUser user = registryUser.getBotUser(id);
        if (user == null) {
            logger.warn("Cant find user {}", id);
            return false;
        }
        UserDialogState state = user.getDialogState();

        if (state == null) {
            logger.debug("Cant find dialog for user {}", id);
            return false;
        }

        state.timeLastAction = System.currentTimeMillis();

        logger.debug("Executing dialog userId={} step=", user.getId(), state.currentStep);

        DialogStatus status = state.dialog.nextStep(ctx, state);
        if (status == DialogStatus.FINISHED) {
            killDialog(id);
        }

        return true;
    }

    private void killDialog(Long userId) {
        BotUser user = registryUser.getBotUser(userId);
        user.setDialogState(null);
    }

    public boolean startDialog(BaseDialog dialog, Long userId, Context ctx) {
        BotUser user = registryUser.getBotUser(userId);
        if (ctx == null) {
            logger.warn("Null context detected for user {}", userId);
            return false;
        }
        if (user == null) return false;
        user.setDialogState(new UserDialogState(dialog));

        logger.debug("Start dialog userId={}", userId);

        return executeDialogIfExists(userId, ctx);
    }
}
