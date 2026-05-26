package com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic;

import java.util.HashMap;
import java.util.Map;

// создается один раз на один диалог и потом удаляется
public class UserDialogState {
    public BaseDialog dialog;

    public short currentStep;
    public Map<String, Object> data;
    public Long timeLastAction;

    public UserDialogState(BaseDialog dialog, Long time) {
        this.dialog = dialog;
        this.currentStep = 0;
        data = new HashMap<>();
        timeLastAction = time;
    }
    public UserDialogState(BaseDialog dialog) {
        this.dialog = dialog;
        this.currentStep = 0;
        data = new HashMap<>();
        timeLastAction = System.currentTimeMillis();
    }
}
