package CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.CommandTrigger;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandLogicTest {

    @Test
    void shouldReturnUserInputWhenMessageExists() {
        Update update = new Update();
        update.setMessage(new Message());   // сам объект Message может быть пустым, важно что не null

        Context ctx = new Context(null, update, null, null);
        assertEquals(CommandTrigger.USER_INPUT, ctx.trigger());
    }

    @Test
    void shouldReturnCallbackWhenCallbackExists() {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());

        Context ctx = new Context(null, update, null, null);
        assertEquals(CommandTrigger.CALLBACK, ctx.trigger());
    }

    @Test
    void shouldReturnMessageEditedWhenEditedMessageExists() {
        Update update = new Update();
        update.setEditedMessage(new Message());

        Context ctx = new Context(null, update, null, null);
        assertEquals(CommandTrigger.MESSAGE_EDITED, ctx.trigger());
    }

    @Test
    void shouldReturnUnknownWhenNoConditionsMatch() {
        Update update = new Update();   // ни одно поле не задано

        Context ctx = new Context(null, update, null, null);
        assertEquals(CommandTrigger.UNKNOWN, ctx.trigger());
    }
}