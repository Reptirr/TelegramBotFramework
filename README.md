# TelegramJavaBot Framework

A lightweight Java framework built on top of the Telegram Bots API for creating structured, dialog-driven Telegram bots.

This framework was designed to avoid rewriting callback handling, dialog state management, and command routing logic for every new bot. It provides a clean architecture with separation of concerns and extensible components.

---

## Features

* Command system (user input and internal callback commands)
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

### 2. Initialize the bot

```java
public class Main {
    public static void main(String[] args) {
        Bot bot = BotBuilder.builder("YOUR_TOKEN")
                .addCommand("/start", new StartCommand())
                .build();

        bot.start();
    }
}
```

---

## Creating a Command

Implement `BaseCommand`:

```java
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;

public class StartCommand implements BaseCommand {
    @Override
    public boolean isForUserInput() {
        return true;
    }

    @Override
    public void execute(Context ctx, String[] args) {
        // example logic
        ctx.getMessenger().sendText(ctx.getMessage().getChat().getId(), "You wrote: " + ctx.getmessage().getText());
    }
}
```

* `isForUserInput()` -> true if command should be triggered by message text
* false if it should only be triggered internally (e.g., callback)

---

## Creating a Dialog

Dialog provides a nextStep() method executed on each user message during an active dialog.
Implement `BaseDialog`:

```java
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.UserDialogState;
import com.Reptir.TelegramJavaBot.Framework.Core.DialogLogic.DialogStatus;

public class MyDialog implements BaseDialog {

    @Override
    public DialogStatus nextStep(Context ctx, UserDialogState dialogState) {
        return switch (dialogState.currentStep) {
            case 0 -> {
                ctx.getMessenger().sendText(ctx.getMessage().getChatId(), "You are on step 1");
                yield DialogStatus.CONTINUE;
            }
            case 1 -> {
                ctx.getMessenger().sendText(ctx.getMessage().getChatId(), "You are on step 2. Finish dialog");
                yield DialogStatus.FINISHED;
            }
            default -> DialogStatus.FINISHED;
        };

    }
}
```

Start dialog inside a command:

```java
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.Context;
import com.Reptir.TelegramJavaBot.Framework.Core.CommandLogic.BaseCommand;

class DialogStartCommand implements BaseCommand {
    void execute(Context ctx, String[] args) {
        if (ctx.getCallback() != null)
            ctx.getDialogManager().startDialog(new MyDialog(), ctx.getMessage().getFrom().getId(), ctx);
    }
}
```

---

##  Notes

* In-memory storage (no persistence by default)
* Suitable for small to medium bots

---
<p align="center">Made by Reptir</p>