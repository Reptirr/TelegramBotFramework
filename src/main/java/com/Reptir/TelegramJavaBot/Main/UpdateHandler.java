package com.Reptir.TelegramJavaBot.Main;

import com.Reptir.TelegramJavaBot.CommandLogic.InformationForCommand;
import com.Reptir.TelegramJavaBot.CommandLogic.TelegramCommandExecutor;
import com.Reptir.TelegramJavaBot.CommandsRealization.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;
import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

@Component
public class UpdateHandler implements LongPollingSingleThreadUpdateConsumer {
    TelegramClient tgClient;
    TelegramCommandExecutor commandExecutor = new TelegramCommandExecutor();

    public UpdateHandler() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String token = prop.getProperty("bot.token");

        tgClient = new OkHttpTelegramClient(
                token
        );

        // Добавление команд
        commandExecutor.AddCommand(new StartCommand());
        commandExecutor.AddCommand(new AnswerTwoFathers());
        commandExecutor.AddCommand(new AnswerMotherDead());
        commandExecutor.AddCommand(new AnswerHasntMother());
        commandExecutor.AddCommand(new AnswerHasMother());
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if (update.hasMessage()) {

            Message message = update.getMessage();

            String input = message.getText();
            String[] args = input.split(" ");
            InformationForCommand info = new InformationForCommand(message, tgClient);

            commandExecutor.ExecByInput(input, info, args);
        } else if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getMessage() instanceof Message message) {
                String callbackData = update.getCallbackQuery().getData();
                String[] parts = callbackData.split(":");

                InformationForCommand information = new InformationForCommand(message, tgClient);
                String commandName = parts[0];
                String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                System.out.println("handle: " + commandName);
                commandExecutor.ExecByInternal(commandName, information, args);
            } else {
                // logging in future
            }
        }
    }
}
