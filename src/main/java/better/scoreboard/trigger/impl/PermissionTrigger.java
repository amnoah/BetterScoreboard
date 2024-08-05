package better.scoreboard.trigger.impl;

import better.scoreboard.trigger.Trigger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

/**
 * This trigger only allow players with the configured permission to use the scoreboard.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class PermissionTrigger extends Trigger {

    private Permission permission = null;

    /**
     * The player can run this trigger if they have the associated permission node.
     */
    @Override
    public boolean canRun(Player player) {
        if (permission == null) return false;
        return player.hasPermission(permission);
    }

    /**
     * Reload this trigger, grabbing the permission node from the configuration.
     */
    @Override
    public void load(ConfigurationSection config) {
        String node = config.getString("permission");

        if (node == null) {
            permission = null;
            return;
        }

        permission = new Permission(node, PermissionDefault.OP);
    }
}
