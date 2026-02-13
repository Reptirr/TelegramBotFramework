package com.Reptir.TelegramJavaBot.CommandLogic;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Command {
    String getName();
    boolean isForUserInput();


    void execute(InformationForCommand info, String[] args) ;


}
