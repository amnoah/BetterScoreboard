package better.scoreboard.condition.impl;

import better.scoreboard.condition.ConditionCheck;

public class NumberConditionCheck implements ConditionCheck {

    private final String mode;

    public NumberConditionCheck(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean compareText(String leftText, String rightText) {
        double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);

        switch (mode) {
            case ">":
                return left > right;
            case ">=":
                return left >= right;
            case "<":
                return left < right;
            case "<=":
                return left <= right;
        }

        return true;
    }
}
