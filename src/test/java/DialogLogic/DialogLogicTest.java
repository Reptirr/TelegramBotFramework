package DialogLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.TelegramContext;
import com.Reptir.TelegramJavaBot.Framework.DialogLogic.BaseDialog;
import com.Reptir.TelegramJavaBot.Framework.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.DialogLogic.DialogStatus;
import com.Reptir.TelegramJavaBot.Framework.DialogLogic.UserDialogState;
import com.Reptir.TelegramJavaBot.Framework.Telegram.BotUser;
import com.Reptir.TelegramJavaBot.Framework.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Telegram.Messenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

public class DialogLogicTest {
    RegistryUser registryUser;
    DialogManager dialogManager;

    @BeforeEach
    void setup() {
        registryUser = new RegistryUser();
        dialogManager = new DialogManager(registryUser);
    }

    @Test
    void dialogShouldExecuting() {

        AtomicBoolean step1 = new AtomicBoolean(false);
        AtomicBoolean step2 = new AtomicBoolean(false);

        BaseDialog dialog = (ctx, dialogState) -> {
            int currentStep = dialogState.currentStep;

            switch (currentStep) {
                case 0:
                    step1.set(true);
                    dialogState.currentStep++;
                    return DialogStatus.CONTINUE;

                case 1:
                    step2.set(true);
                    return DialogStatus.FINISHED;
            }

            return DialogStatus.FINISHED;
        };

        User user = new User(1L, "test", false);
        registryUser.addBotUserIfNotRegistered(new BotUser(user));

        Context ctx = new Context(
                new TelegramContext(null, new Messenger(new OkHttpTelegramClient(""))),
                dialogManager,
                registryUser
        );

        dialogManager.startDialog(dialog, user.getId(), ctx);
        assertTrue(step1.get());

        dialogManager.executeDialogIfExists(user.getId(), ctx);
        assertTrue(step2.get());
    }

    @Test
    void dialogShouldFinished() {

        AtomicBoolean step1 = new AtomicBoolean(false);
        AtomicBoolean step2 = new AtomicBoolean(false);

        BaseDialog dialog = new BaseDialog() {
            @Override
            public DialogStatus nextStep(Context ctx, UserDialogState dialogState) {
                int currentStep = dialogState.currentStep;

                switch (currentStep) {
                    case 0:
                        step1.set(true);
                        dialogState.currentStep++;
                        return DialogStatus.FINISHED;

                    case 1:
                        step2.set(true);
                        return DialogStatus.FINISHED;
                }

                return DialogStatus.FINISHED;
            }
        };

        Context ctx = new Context(
                new TelegramContext(null, new Messenger(new OkHttpTelegramClient(""))),
                dialogManager,
                registryUser
        );

        BotUser botUser = new BotUser(new User(1L, "test", false));
        registryUser.addBotUserIfNotRegistered(botUser);

        dialogManager.startDialog(dialog, 1L, ctx);
        assertTrue(step1.get());

        dialogManager.executeDialogIfExists(1L, ctx);
        assertFalse(step2.get());
    }

    @Test
    void dialogShouldDeleteAfterFinish() {
        BaseDialog dialog = new BaseDialog() {
            @Override
            public DialogStatus nextStep(Context ctx, UserDialogState dialogState) {
                return DialogStatus.FINISHED;
            }
        };

        Context ctx = new Context(
                new TelegramContext(null, new Messenger(new OkHttpTelegramClient(""))),
                dialogManager,
                registryUser
        );

        BotUser botUser = new BotUser(new User(1L, "test", false));
        registryUser.addBotUserIfNotRegistered(botUser);

        dialogManager.startDialog(dialog, 1L, ctx);
        assertNull(registryUser.getBotUser(1L).getDialogState());
    }

    @Test
    void dialogManagerShouldReturnFalseAfterIncorrectUserId() {
        assertFalse(dialogManager.startDialog(null, 198766985L, null));
        assertFalse(dialogManager.executeDialogIfExists(123412341234L, null));
    }

}
