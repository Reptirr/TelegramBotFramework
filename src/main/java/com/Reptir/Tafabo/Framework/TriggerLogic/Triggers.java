package com.Reptir.Tafabo.Framework.TriggerLogic;

public class Triggers {
    public static Trigger and(Trigger... triggers) {
        return update -> {
            for (Trigger trigger : triggers) {
                if (!trigger.match(update)) return false;
            }
            return true;
        };
    }

    public static Trigger or(Trigger... triggers) {
        return update -> {
            for (Trigger trigger : triggers) {
                if (trigger.match(update)) return true;
            }
            return false;
        };
    }
}