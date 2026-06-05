package CommandLogic;

import com.Reptir.TelegramJavaBot.Framework.ContextLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.TelegramContext;
import com.Reptir.TelegramJavaBot.Framework.ContextLogic.UpdateType;
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

        Context ctx = new Context(new TelegramContext(update, null), null, null);
        assertEquals(UpdateType.USER_INPUT, ctx.updateType());
    }

    @Test
    void shouldReturnCallbackWhenCallbackExists() {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());

        Context ctx = new Context(new TelegramContext(update, null), null, null);
        assertEquals(UpdateType.CALLBACK, ctx.updateType());
    }

    @Test
    void shouldReturnMessageEditedWhenEditedMessageExists() {
        Update update = new Update();
        update.setEditedMessage(new Message());

        Context ctx = new Context(new TelegramContext(update, null), null, null);
        assertEquals(UpdateType.MESSAGE_EDITED, ctx.updateType());
    }

    @Test
    void shouldReturnUnknownWhenNoConditionsMatch() {
        Update update = new Update();   // ни одно поле не задано

        Context ctx = new Context(new TelegramContext(update, null), null, null);
        assertEquals(UpdateType.UNKNOWN, ctx.updateType());
    }
}