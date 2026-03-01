package example.Realizations.CommandsRealization;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;
import com.Reptir.TelegramJavaBot.Framework.Core.Telegram.Context;

// another command which execute by callback "action1"
public class ActionCommand implements BaseCommand {
    @Override
    public String getName() {
        return "action1";
    }

    @Override
    public boolean isForUserInput() {
        return false;
    }

    @Override
    public void execute(Context ctx, String[] args) {
        ctx.edit("Ta-daam! I`m was editing that message right now.", null);
    }
}
