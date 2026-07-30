# TelegramJavaBot Framework

A lightweight Java framework built on top of the Telegram Bots API for creating structured, dialog-driven Telegram bots.

This framework was designed to avoid rewriting callback handling, dialog state management, and command routing logic for every new bot. It provides a clean architecture with separation of concerns and extensible components.

---

## Features

* Command system (Trigger -> BaseCommand)
* Dialog system with step-based state management
* Automatic dialog timeout handling
* Inline keyboard builder
* Centralized registries (users, commands, dialogs, threads)
* Virtual thread execution (Project Loom)
* Builder-style bot initialization
* Clear separation between Core and Realizations


---

## Quick Start

### 1. Create a bot in Telegram

Get your bot token from `@BotFather`.

### 2. Initialize the bot and add command

```java
public class Main {
    public static void main(String[] args) {
        TelegramBot bot = new TelegramBot("YOUR_TOKEN");

        bot.addCommand(Triggers.command("/start"), ctx -> ctx.tgContext().messenger().sendText(ctx.chatId(), "start"));
        
        bot.start();
    }
}
```

---

## Creating a Command

Implement `BaseCommand`:

```java
public class StartCommand implements BaseCommand {
    @Override
    public void execute(Context ctx, String[] args) {
        // example logic
        ctx.tgContext().messenger().sendText(ctx.getMessage().getChat().getId(), "hi");
    }
}
```

---

##  Notes

* In-memory storage (no persistence by default)
* Suitable for small to medium bots

---
<p align="center">Made by Reptir</p>