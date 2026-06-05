package com.Reptir.TelegramJavaBot.Framework.DialogLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;


public interface BaseDialog {
     /**
      * Выполняет текущий шаг диалога для пользователя.
      *
      * @param ctx контекст сообщения
      * @param dialogState состояние данного диалога
      * @return DialogStatus с соответственным выбором
      */
     DialogStatus nextStep(Context ctx, UserDialogState dialogState);
}
