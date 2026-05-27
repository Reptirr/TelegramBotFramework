package com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.TriggerCollections;

import com.Reptir.TelegramJavaBot.Framework.Core.TriggerLogic.Trigger;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TriggerOr implements Trigger {
    private final Set<Trigger> triggers = new HashSet<>();

    public TriggerOr(Trigger[] triggers) {
        this.triggers.addAll(List.of(triggers));
    }

    public static TriggerOr of(Trigger... triggers) {
        return new TriggerOr(triggers);
    }

    @Override
    public boolean match(Update update) {
        for (Trigger trigger : triggers) {
            if (trigger.match(update)) return true;
        }

        return false;
    }
}
