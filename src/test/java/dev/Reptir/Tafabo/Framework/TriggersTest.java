package dev.Reptir.Tafabo.Framework;

import dev.Reptir.Tafabo.Framework.TriggerLogic.Trigger;
import dev.Reptir.Tafabo.Framework.TriggerLogic.Triggers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TriggersTest {

    @Test
    void andReturnsTrueWhenAllTriggersMatch() {
        Trigger<String> first = value -> value.length() > 2;
        Trigger<String> second = value -> value.startsWith("a");

        assertTrue(Triggers.and(first, second).match("abc"));
    }

    @Test
    void andReturnsFalseWhenOneTriggerDoesNotMatch() {
        Trigger<String> first = value -> true;
        Trigger<String> second = value -> false;

        assertFalse(Triggers.and(first, second).match("value"));
    }

    @Test
    void andDoesNotEvaluateLaterTriggersAfterFailure() {
        boolean[] evaluated = {false};

        Trigger<String> result = Triggers.and(
                value -> false,
                value -> {
                    evaluated[0] = true;
                    return true;
                }
        );

        assertFalse(result.match("value"));
        assertFalse(evaluated[0]);
    }

    @Test
    void orReturnsTrueWhenAtLeastOneTriggerMatches() {
        Trigger<String> first = value -> false;
        Trigger<String> second = value -> true;

        assertTrue(Triggers.or(first, second).match("value"));
    }

    @Test
    void orReturnsFalseWhenNoTriggerMatches() {
        Trigger<String> first = value -> false;
        Trigger<String> second = value -> false;

        assertFalse(Triggers.or(first, second).match("value"));
    }

    @Test
    void orDoesNotEvaluateLaterTriggersAfterSuccess() {
        boolean[] evaluated = {false};

        Trigger<String> result = Triggers.or(
                value -> true,
                value -> {
                    evaluated[0] = true;
                    return false;
                }
        );

        assertTrue(result.match("value"));
        assertFalse(evaluated[0]);
    }

    @Test
    void emptyAndMatchesEverything() {
        assertTrue(Triggers.and().match("value"));
    }

    @Test
    void emptyOrMatchesNothing() {
        assertFalse(Triggers.or().match("value"));
    }
}
