package example.Realizations.CommandsRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.TelegramWrappers;
import example.Realizations.DialogRealization.JustDialog;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DialogCommand implements BaseCommand {
    @Override
    public String getName() {
        return "/dialog";
    }

    @Override
    public boolean isForUserInput() {
        return true;
    }

    @Override
    public void execute(Context ctx, String[] args) {
        try {
            ctx.getTgClient().execute(TelegramWrappers.sendMessage(ctx.getMessage().getChatId(), "Начинаю диалог...."));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        ctx.getDialogManager().startDialog(new JustDialog(), ctx.getMessage().getFrom().getId(), ctx);
    }
}
