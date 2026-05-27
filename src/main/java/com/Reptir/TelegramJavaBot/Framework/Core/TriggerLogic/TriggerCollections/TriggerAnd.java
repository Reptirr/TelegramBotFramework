package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.TriggerCollections;

import com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.Trigger;
import org.jetbrains.annotations.Contract;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TriggerAnd implements Trigger {
    private final Set<Trigger> triggers = new HashSet<>();

    public TriggerAnd(Trigger[] triggers) {
        this.triggers.addAll(List.of(triggers));
    }

    public static TriggerAnd of(Trigger... triggers) {
        return new TriggerAnd(triggers);
    }

    @Override
    public boolean match(Update update) {
        for (Trigger trigger : triggers) {
            if (!trigger.match(update)) return false;
        }

        return true;
    }
}
