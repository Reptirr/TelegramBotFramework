package com.Reptir.TelegramJavaBot.Framework.Core.ContextLogic;

public enum ChatType {
    PRIVATE,
    GROUP,
    CHANNEL;

    public static ChatType map(String type) {
        return switch (type) {
            case "private" -> ChatType.PRIVATE;
            case "group", "supergroup" -> ChatType.GROUP;
            case "channel" -> ChatType.CHANNEL;
            default -> null;
        };
    }
}
