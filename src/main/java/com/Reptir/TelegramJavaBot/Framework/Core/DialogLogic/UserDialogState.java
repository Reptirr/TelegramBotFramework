package com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic;

import java.util.HashMap;
import java.util.Map;

public class UserDialogState {
    public short currentStep;
    public BaseDialog dialog;
    public Long userId;
    public Map<String, Object> data;
    public Long timeLastAction;

    public UserDialogState(BaseDialog dialog, Long userId, Long time) {
        this.dialog = dialog;
        this.userId = userId;
        this.currentStep = 0;
        data = new HashMap<>();
        timeLastAction = time;
    }
}
