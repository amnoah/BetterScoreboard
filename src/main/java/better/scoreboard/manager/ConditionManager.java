package better.scoreboard.manager;

import better.scoreboard.condition.Condition;
import better.scoreboard.condition.ConditionCheck;

import java.util.HashMap;
import java.util.Map;

public class ConditionManager {

    private final static ConditionCheck defaultCondition;

    private final static Map<String, Condition> CONDITIONS = new HashMap<>();
    private final static Map<String, ConditionCheck> CONDITION_CHECKS = new HashMap<>();

    static {
        defaultCondition = (((leftText, rightText) -> true));
    }

    /*
     * Conditions Map methods.
     */

    /**
     * Add a Board to the tracking set.
     */
    public static void addCondition(String name, Condition condition) {
        CONDITIONS.put(name, condition);
    }

    /**
     * Remove all Board objects from the tracking set.
     * NOTE: This will NOT remove the boards from inside BoardUser objects.
     */
    public static void clear() {
        CONDITIONS.clear();
    }

    public static Condition getCondition(String name) {
        return CONDITIONS.get(name.toLowerCase());
    }

    /*
     * ConditionChecks Map methods.
     */

    public static void registerConditionCheck(String name, ConditionCheck conditionCheck) {
        CONDITION_CHECKS.put(name, conditionCheck);
    }

    public static ConditionCheck retrieveConditionCheck(String name) {
        if (name == null) return defaultCondition;
        ConditionCheck conditionCheck = CONDITION_CHECKS.get(name.toLowerCase());
        if (conditionCheck == null) return defaultCondition;
        return conditionCheck;
    }
}
