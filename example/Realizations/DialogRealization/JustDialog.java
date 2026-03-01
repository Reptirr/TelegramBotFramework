package example.Realizations.DialogRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.BaseDialog;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryDialogState;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.TelegramWrappers;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class JustDialog implements BaseDialog {
    @Override
    public boolean nextStep(Long userId, Context ctx, String input, RegistryDialogState registryDialogState) {
        UserDialogState state = registryDialogState.get(userId);

        try {


            switch (state.currentStep) {
                case 0:
                    ctx.getTgClient().execute(TelegramWrappers.sendMessage(ctx.getMessage().getChatId(), "Введите свое имя"));
                    state.currentStep = 1;
                    return false;

                case 1:
                    state.data.put("name", input);
                    ctx.getTgClient().execute(TelegramWrappers.sendMessage(ctx.getMessage().getChatId(), "Введите свой любимый цвет: "));
                    state.currentStep = 2;
                    return false;

                case 2:
                    state.data.put("color", input);
                    ctx.getTgClient().execute(TelegramWrappers.sendMessage(ctx.getMessage().getChatId(), "Делаю докс..."));

                    for (int i = 0; i < 4; i++) {
                        ctx.getTgClient().execute(TelegramWrappers.sendMessage(ctx.getMessage().getChatId(), String.valueOf(100 / 4 * (i + 1))));
                    }

                    ctx.getTgClient().execute(TelegramWrappers.sendMessage(ctx.getMessage().getChatId(), "Докс сделан! ваши данные: \n" +
                            "Имя: " + state.data.get("name") +
                            "\nВаш любимый цвет: " + state.data.get("color") +
                            "\nТвой айди: " + userId));

                    return true;
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
