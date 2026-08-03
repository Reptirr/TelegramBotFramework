package dev.Reptir.Tafabo.Framework.TriggerLogic;

public interface Trigger<U> {
    boolean match(U update);
}
