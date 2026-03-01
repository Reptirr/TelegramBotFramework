package com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryDialogState;

/*
* В последовательности хранятся степы: {1=func1, 2=func2} а в аргументах функций стоят Message
*
*/

public interface BaseDialog {
     /**
      * Выполняет текущий шаг диалога для пользователя.
      *
      * @param userId                  ID пользователя
      * @param ctx                     контекст сообщения
      * @param input                   введенные пользователем данные
      * @param registryDialogState регистр состояний диалогов
      * @return true, если диалог завершён, false если продолжается
      */
     boolean nextStep(Long userId, Context ctx, String input, RegistryDialogState registryDialogState);
}
