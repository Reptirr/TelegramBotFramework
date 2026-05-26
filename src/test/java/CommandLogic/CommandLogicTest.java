package CommandLogic;


import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandExecutor;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogManager;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Registries.RegistryUser;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class CommandLogicTest {
    @Test
    void internalCommandShouldExecute() {
        var registry = new RegistryCommand();
        var executor = new CommandExecutor(registry);

        var executed = new AtomicBoolean(false);

        registry.register("test", new BaseCommand() {
            @Override
            public boolean isForUserInput() {
                return false;
            }

            @Override
            public void execute(Context ctx, String[] args) {
                executed.set(true);
            }
        });

        executor.ExecByInput("test", null, new String[0]); // чекаем что не реагирует на инпут от пользователя
        assertFalse(executed.get());

        executor.ExecByInternal("test", null, new String[0]);
        assertTrue(executed.get());
    }
    @Test
    void inputCommandShouldExecute() {
        var registry = new RegistryCommand();
        var executor = new CommandExecutor(registry);

        var executed = new AtomicBoolean(false);

        registry.register("test", new BaseCommand() {
            @Override
            public boolean isForUserInput() {
                return true;
            }

            @Override
            public void execute(Context ctx, String[] args) {
                executed.set(true);
            }
        });

        executor.ExecByInput("test", null, new String[0]);
        assertTrue(executed.get());
    }

    @Test
    void commandShouldReceiveArguments() {
        var registry = new RegistryCommand();
        var executor = new CommandExecutor(registry);

        var correctArgs = new AtomicBoolean(false);

        registry.register("test", new BaseCommand() {

            @Override
            public boolean isForUserInput() {
                return true;
            }

            @Override
            public void execute(Context ctx, String[] args) {
                correctArgs.set(
                        args.length == 1 &&
                                args[0].equals("testString")
                );
            }
        });

        executor.ExecByInput("test", null, new String[]{"testString"});

        assertTrue(correctArgs.get());
    }

    @Test
    void alertFromCommandShouldExecute() {
        var registry = new RegistryCommand();
        var executor = new CommandExecutor(registry);

        var alertExecuted = new AtomicBoolean(false);

        registry.register("test", new BaseCommand() {

            @Override
            public boolean isForUserInput() {
                return true;
            }

            @Override
            public void execute(Context ctx, String[] args) {

            }

            @Override
            public void executeAlert(Context ctx) {
                alertExecuted.set(true);
            }
        });

        Context ctx = new Context(
                new Message(),
                new OkHttpTelegramClient("123"),
                new CallbackQuery(),
                new DialogManager(new RegistryUser()),
                new RegistryUser()
        );

        executor.ExecByInput("test", ctx, null);
        assertTrue(alertExecuted.get());
    }


}
