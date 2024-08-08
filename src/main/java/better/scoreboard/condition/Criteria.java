package better.scoreboard.condition;

import better.scoreboard.manager.ConditionManager;
import better.scoreboard.util.Line;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Criteria {

    private final Permission permission;
    private final Line leftText, rightText;
    private final ConditionCheck conditionCheck;

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
                conditionCheck = ConditionManager.retrieveConditionCheck("default");
                break;
        }

        leftText = new Line(elements[0]);
    }

    public boolean canRun(Player player) {
        if (permission != null) return player.hasPermission(permission);
        if (conditionCheck == null) return true;
        return conditionCheck.compareText(leftText.getText(player), rightText.getText(player));
    }
}
