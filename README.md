# Tafabo Core

[![](https://jitpack.io/v/Tafabo/tafabo-core.svg)](https://jitpack.io/#Tafabo/tafabo-core)
 
Tafabo Core is a lightweight, event-driven Java framework for building bots and other things.

It is **platform-independent** and communicates through adapters that connect any messaging system.

---

## Features

* Event-driven architecture
* Platform-agnostic design
* Trigger-based command routing
* Virtual-thread execution (Java 22+)
* Adapter-based extensibility
* Minimal and lightweight core

---

## Core Concepts

* **Update** — incoming event from a messaging platform
* **Messenger** — abstraction for sending messages
* **Command** — unit of business logic
* **Trigger** — condition that determines command execution
* **Context** — runtime data passed to commands
* **Adapter** — bridge between a platform and Tafabo Core

---

## How it works

1. A platform receives an update
2. The adapter forwards it to the core
3. The router matches commands using triggers
4. Matching commands are executed in virtual threads
5. Each command receives a `Context` with runtime data

---

## Example

```Java
//
// Code from tafabo-telegram
//

public class TelegramMessenger {
    private final OkHttpTelegramClient tgClient;

    public TelegramMessenger(String token) {
        this.tgClient = new OkHttpTelegramClient(token);
    }


    public void sendText(long id, String text) {
        try {
            SendMessage sendMessage = new SendMessage(String.valueOf(id), text);
            tgClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Send text to '{}' failed: {}", id, e.toString());
        }
    }
}



public static class TelegramBot extends TafaboAdapter<Update, TelegramMessenger> {

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
    protected void onStart() throws TelegramApiException {
        app = new TelegramBotsLongPollingApplication();
        app.registerBot(token, this::onTelegramUpdate);
    }

    @Override
    protected void onStop() throws TelegramApiException {
        app.stop();
    }
}
```

Use it: 

```Java
public static void main(String[] args) {
    var bot = new TelegramBot("YOUR_TOKEN");

    bot.addCommand(
            update -> update.hasMessage() && update.getMessage().hasText(),
            ctx -> ctx.messenger().sendText(ctx.update().getMessage().getChatId(), "you wrote: " + ctx.update().getMessage().getText())
    );
}
```

---


## Installation by Maven

### Step 1. Use jitpack:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Step 2. Import tafabo-core:

```xml
<dependency>
    <groupId>com.github.Tafabo</groupId>
    <artifactId>tafabo-core</artifactId>
    <version>v1.9</version>
</dependency>
```

---

## Requirements

* Java 22 or higher
* Maven

---

## Notes

* Concurrency is handled using Java Virtual Threads
* No built-in persistence layer is provided
* The framework is extended exclusively via adapters
* The core is intentionally minimal and unopinionated

---

## License

MIT

---

<p align="center">Made by Reptirr</p>
