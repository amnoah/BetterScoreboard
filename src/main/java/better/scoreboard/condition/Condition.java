package better.scoreboard.condition;

import better.scoreboard.BetterScoreboard;
import better.scoreboard.util.Line;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents and handles a conditional placeholder in the configuration.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.1.0
 */
public class Condition {

    private enum Mode {
        AND,
        OR
    }

    private final BetterScoreboard plugin;
    private final String name;
    private final List<Criteria> criteria = new ArrayList<>();
    private final Line falseLine, trueLine;
    private final Mode mode;

    /**
     * Initialize the Condition object.
     */
    public Condition(BetterScoreboard plugin, ConfigurationSection section) {
        this.plugin = plugin;
        this.name = section.getName();

        for (String string : section.getStringList("criteria")) criteria.add(new Criteria(string));
        switch (section.getString("mode", "and").toLowerCase()) {
            case "or":
                this.mode = Mode.OR;
                break;
            case "and":
            default:
                this.mode = Mode.AND;
                break;
        }

        falseLine = new Line(section.getString("false"));
        trueLine = new Line(section.getString("true"));
    }

    /**
     * Return the proper text that this condition will produce.
     */
    public String getText(Player player) {
        if (mode.equals(Mode.AND)) {
            for (Criteria criteria : this.criteria) if (!criteria.canRun(player)) return falseLine.getText(player);
            return trueLine.getText(player);
        } else {
            for (Criteria criteria : this.criteria) if (criteria.canRun(player)) return trueLine.getText(player);
            return falseLine.getText(player);
        }
    }

    /**
     * This represents a specific criteria of a configuration.
     *
     * @Author: am noah
     * @Since: 1.0.0
     * @Updated: 1.1.0
     */
    private class Criteria {

        private final Permission permission;
        private final Line leftText, rightText;
        private final ConditionCheck conditionCheck;

        private boolean sentErrorMessage = false;

        /**
         * Initialize the Criteria object.
         */
        public Criteria(String line) {
            if (line.startsWith("permission:")) {
                permission = new Permission(line.substring(11), PermissionDefault.OP);
                conditionCheck = null;
                leftText = null;
                rightText = null;
                return;
            } else permission = null;

            String[] elements = line.split(" ", 3);

            switch (elements.length) {
                case 3:
                    rightText = new Line(elements[2]);
                    conditionCheck = ConditionManager.retrieveConditionCheck(elements[1]);
                    break;
                case 2:
                    rightText = new Line("");
                    conditionCheck = ConditionManager.retrieveConditionCheck(elements[1]);
                    break;
                default:
                    rightText = new Line("");
                    conditionCheck = ConditionManager.retrieveConditionCheck(null);
                    break;
            }

            leftText = new Line(elements[0]);
        }

        /**
         * Return whether the player passes this criteria.
         */
        public boolean canRun(Player player) {
            if (permission != null) return player.hasPermission(permission);
            if (conditionCheck == null) return true;
            try {
                return conditionCheck.compareText(leftText.getText(player), rightText.getText(player));
            } catch (NumberFormatException e) {
                if (!sentErrorMessage) {
                    plugin.getLogger().warning("Could not parse placeholder as number on condition " + name + "!");
                    plugin.getLogger().warning("Left placeholder text: " + leftText.getText(player));
                    plugin.getLogger().warning("Right placeholder text: " + rightText.getText(player));
                    sentErrorMessage = true;
                }
            }

            return true;
        }
    }
}
