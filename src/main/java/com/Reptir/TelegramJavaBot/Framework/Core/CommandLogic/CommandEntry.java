package com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic;

import java.util.EnumSet;

public record CommandEntry(BaseCommand command, EnumSet<CommandTrigger> triggers) {

}
