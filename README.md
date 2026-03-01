# TelegramJavaBot Framework

A lightweight Java framework built on top of the Telegram Bots API for creating structured, dialog-driven Telegram bots.

This framework was designed to avoid rewriting callback handling, dialog state management, and command routing logic for every new bot. It provides a clean architecture with separation of concerns and extensible components.

---

## ✨ Features

* Command system (user input & internal callback commands)
* Dialog system with step-based state management
* Automatic dialog timeout handling
* Inline keyboard builder
* Centralized registries (users, commands, dialogs, threads)
* Virtual thread execution (Project Loom)
* Builder-style bot initialization
* Clear separation between Core and Realizations

---

## 📁 Project Structure

```
Framework
 └── Core
      ├── CommandLogic
      ├── DialogLogic
      ├── Handlers
      ├── MenuLogic
      ├── Registries
      ├── Telegram
      └── TimeoutLogic

Realizations
 ├── CommandsRealization
 ├── DialogRealization
 └── MenuRealization
```

* **Core** — framework internals
* **Realizations** — your actual bot implementation

---

## 🚀 Quick Start

### 1. Create a bot in Telegram

Get your bot token from `@BotFather`.

### 2. Initialize the bot

```java
public class Main {
    public static void main(String[] args) {
        Bot bot = BotBuilder.builder("YOUR_TOKEN")
                .command(new StartCommand())
                .command(new DialogCommand())
                .command(new ActionCommand())
                .build();

        new Thread(bot::start).start();
    }
}
```

---

## 🧩 Creating a Command

Implement `BaseCommand`:

```java
public class StartCommand implements BaseCommand {

    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public boolean isForUserInput() {
        return true;
    }

    @Override
    public void execute(Context ctx, String[] args) {
        // your logic here
    }
}
```

* `isForUserInput()` → true if command should be triggered by message text
* false if it should only be triggered internally (e.g., callback)

---

## 💬 Creating a Dialog

Implement `BaseDialog`:

```java
public class MyDialog implements BaseDialog {

    @Override
    public boolean nextStep(Long userId, Context ctx, String input, RegistryDialogState registry) {
        UserDialogState state = registry.get(userId);

        switch (state.currentStep) {
            case 0:
                // ask something
                state.currentStep = 1;
                return false;

            case 1:
                // handle input
                return true; // finish dialog
        }

        return true;
    }
}
```

Start dialog inside a command:

```java
ctx.getDialogManager().startDialog(new MyDialog(), userId, ctx);
```

---

## ⏳ Dialog Timeout

Dialogs are automatically removed after inactivity.

You can configure timeout duration:

```java
bot.setTimeoutDialog((short) 30); // seconds
```

---

## 🧠 Architecture Overview

* `Bot` — main facade
* `BotBuilder` — fluent configuration
* `Context` — wrapper around Telegram update data
* `Registry*` — centralized state containers
* `DialogManager` — dialog execution controller
* `TelegramCommandExecutor` — command dispatcher
* `TimeoutService` — dialog expiration logic

---

## 🔧 Threading Model

Commands are executed using virtual threads:

```
Executors.newVirtualThreadPerTaskExecutor()
```

Each update is processed asynchronously to prevent blocking.

---

## 🎯 Design Goals

* Avoid repetitive Telegram boilerplate
* Provide structured dialog handling
* Keep architecture modular
* Stay lightweight and framework-oriented
* Make future bots easier to build

---

## 📌 Notes

* In-memory storage (no persistence by default)
* Suitable for small to medium bots
* Easily extendable for custom storage or middleware

---

## 🛠 Possible Future Improvements

* Middleware support
* Event system
* Pluggable storage (Redis / Database)
* Metrics & monitoring
* Rate limiting

---

## 📜 License

Free to use and modify.
