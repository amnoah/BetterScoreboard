package better.scoreboard.condition.impl;

import better.scoreboard.condition.ConditionCheck;

public class StringConditionCheck implements ConditionCheck {

    private final String mode;

    public StringConditionCheck(String mode) {
        this.mode = mode;
    }

    @Override
    public boolean compareText(String leftText, String rightText) {
        switch (mode) {
            case "=":
                return leftText.equalsIgnoreCase(rightText);
            case "==":
                return leftText.equals(rightText);
            case "!=":
                return !leftText.equalsIgnoreCase(rightText);
            case "!==":
                return !leftText.equals(rightText);
            case "|-":
                return leftText.startsWith(rightText);
            case "-|":
                return leftText.endsWith(rightText);
            case "$":
                return leftText.toLowerCase().contains(rightText.toLowerCase());
            case "$$":
                return leftText.contains(rightText);

        }
        return false;
    }
}
