import dev.Reptir.Tafabo.Framework.AdapterLogic.TafaboAdapter;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.Middleware;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareArg;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.MiddlewareRegistrator;
import dev.Reptir.Tafabo.Framework.MiddlewareLogic.PipelineState;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class Main {

    public static class TelegramMessenger {
        private final OkHttpTelegramClient tgClient;

        public TelegramMessenger(String token) {
            this.tgClient = new OkHttpTelegramClient(token);
        }


        public void sendText(long id, String text) {
            try {
                SendMessage sendMessage = new SendMessage(String.valueOf(id), text);
                tgClient.execute(sendMessage);
            } catch (TelegramApiException e) {

            }
        }
    }



    public static class TelegramAdapter extends TafaboAdapter<Update, TelegramMessenger> {

        private TelegramBotsLongPollingApplication app;
        private final String token;

        public TelegramAdapter(String token) {
            super(new TelegramMessenger(token));
            this.token = token;
        }

        private void onTelegramUpdate(List<Update> updates) {
            updates.forEach(this::onUpdate);
        }

        @Override
        protected void onStart() {
            app = new TelegramBotsLongPollingApplication();
            try {
                app.registerBot(token, this::onTelegramUpdate);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onStop() {
            try {
                app.stop();
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        TelegramAdapter adapter = new TelegramAdapter("8537210836:AAHdr06u3S31axhgF_vaGtRlCiYndHwearo");

        adapter.addMiddleware(new MiddlewareRegistrator<>() {
            @Override
            protected Middleware<MiddlewareArg<Update, Update, TelegramMessenger>, PipelineState> onBeforeCommandSearching() {
                return new Middleware<>(
                        0,
                        arg -> {
                            var ctx = arg.context();
                            System.out.println("Received update by user '@" + ctx.update().getMessage().getFrom().getUserName());
                            if (ctx.update().getMessage().getText().equals("stop")) {
                                return PipelineState.STOP;
                            } else {
                                return PipelineState.CONTINUE;
                            }
                        }
                );
            }
        });

        adapter.addCommand(Update::hasMessage, ctx -> ctx.messenger().sendText(ctx.update().getMessage().getChatId(), "so"));

        adapter.start();
    }
}
