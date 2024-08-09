package better.scoreboard.condition;

/**
 * This determines whether a Condition will return the true or false text.
 *
 * @Author: am noah
 * @Since: 1.1.0
 * @Updated: 1.1.0
 */
public interface ConditionCheck {

    /**
     * Can be implemented as a lambda.
     * Example: ((leftText, rightText) -> leftText.equals(rightText))
     */
    boolean compareText(String leftText, String rightText);
}
