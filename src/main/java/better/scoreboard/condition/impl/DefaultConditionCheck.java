package better.scoreboard.condition.impl;

import better.scoreboard.condition.ConditionCheck;

public class DefaultConditionCheck implements ConditionCheck {

    @Override
    public boolean compareText(String leftText, String rightText) {
        return true;
    }
}
