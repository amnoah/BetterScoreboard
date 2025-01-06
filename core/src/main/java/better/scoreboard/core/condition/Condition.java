package better.scoreboard.core.condition;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.bridge.ConfigSection;
import better.scoreboard.core.display.Line;
import com.github.retrooper.packetevents.protocol.player.User;

import java.util.ArrayList;
import java.util.List;

public class Condition {

    private enum Mode {
        AND,
        OR
    }

    private final String name;
    private final List<Criteria> criteria = new ArrayList<>();
    private final Line falseLine, trueLine;
    private final Mode mode;

    public Condition(BetterScoreboard plugin, ConfigSection config) {
        this.name = config.getName();

        for (String string : config.getList(String.class, "criteria")) criteria.add(new Criteria(plugin, string));

        if (config.getObject(String.class, "mode", "and").equalsIgnoreCase("or")) this.mode = Mode.OR;
        else this.mode = Mode.AND;

        falseLine = new Line(plugin, config.getObject(String.class, "no", null));
        trueLine = new Line(plugin, config.getObject(String.class, "yes", null));
    }

    public boolean isTrue(User user) {
        if (mode.equals(Mode.AND)) {
            for (Criteria criteria : this.criteria) if (!criteria.canRun(user)) return false;
            return true;
        } else {
            for (Criteria criteria : this.criteria) if (criteria.canRun(user)) return true;
            return false;
        }
    }

    public String getText(User user) {
        return isTrue(user) ? trueLine.getText(user) : falseLine.getText(user);
    }

    public static void load(BetterScoreboard plugin, ConfigSection config) {
        config = config.getConfigSection("conditions");
        if (config != null) {
            for (String condition : config.getChildren()) {
                ConfigSection section = config.getConfigSection(condition);

                if (section == null) {
                    plugin.getLogger().logWarning("Could not resolve condition named \"" + condition + "\" in config.yml!");
                    continue;
                }

                ConditionManager.addCondition(condition.toLowerCase(), new Condition(plugin, section));
            }
        }
    }

    private class Criteria {

        private final BetterScoreboard plugin;
        private final String permission;
        private final Line leftText, rightText;
        private final ConditionCheck conditionCheck;

        private boolean sentErrorMessage = false;

        public Criteria(BetterScoreboard plugin, String line) {
            this.plugin = plugin;

            if (line.startsWith("permission:")) {
                permission = line.substring(11);
                conditionCheck = null;
                leftText = null;
                rightText = null;
                return;
            } else permission = null;

            String[] elements = line.split(" ", 3);

            switch (elements.length) {
                case 3:
                    rightText = new Line(plugin, elements[2]);
                    conditionCheck = ConditionManager.retrieveConditionCheck(elements[1]);
                    break;
                case 2:
                    rightText = new Line(plugin, "");
                    conditionCheck = ConditionManager.retrieveConditionCheck(elements[1]);
                    break;
                default:
                    rightText = new Line(plugin, "");
                    conditionCheck = ConditionManager.retrieveConditionCheck(null);
                    break;
            }

            leftText = new Line(plugin, elements[0]);
        }

        public boolean canRun(User user) {
            if (permission != null) return plugin.getData().hasPermission(user, permission);
            if (conditionCheck == null) return true;
            try {
                return conditionCheck.compareText(leftText.getText(user), rightText.getText(user));
            } catch (NumberFormatException e) {
                if (!sentErrorMessage) {
                    plugin.getLogger().logWarning("Could not parse placeholder as number on condition " + name + "!");
                    plugin.getLogger().logWarning("Left placeholder text: " + leftText.getText(user));
                    plugin.getLogger().logWarning("Right placeholder text: " + rightText.getText(user));
                    sentErrorMessage = true;
                }
            }

            return true;
        }
    }
}
