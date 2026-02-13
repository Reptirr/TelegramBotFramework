package com.Reptir.TelegramJavaBot.Main;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class TelegramBot implements SpringLongPollingBot {
    private final UpdateHandler updateConsumer;

    public TelegramBot(UpdateHandler updateConsumer) {
        this.updateConsumer = updateConsumer;
    }


    @Override
    public String getBotToken() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream("config.properties")) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return prop.getProperty("bot.token");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
