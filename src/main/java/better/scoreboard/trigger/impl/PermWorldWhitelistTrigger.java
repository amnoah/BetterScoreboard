package better.scoreboard.trigger.impl;

import better.scoreboard.trigger.Trigger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.List;

/**
 * This trigger only allow players in the configured worlds who have the configured permission to use
 * the scoreboard.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class PermWorldWhitelistTrigger extends Trigger {

    private Permission permission = null;
    private List<String> worlds = null;

    /**
     * The player can run this trigger if they're in one of the configured worlds and have the permission.
     */
    @Override
    public boolean canRun(Player player) {
        if (permission == null) return false;
        if (!player.hasPermission(permission)) return false;
        for (String world : worlds) if (player.getWorld().getName().equalsIgnoreCase(world)) return true;
        return false;
    }

    /**
     * Reload this trigger, grabbing the worlds and permission node from the configuration.
     */
    @Override
    public void load(ConfigurationSection config) {
        worlds = config.getStringList("worlds");

        String node = config.getString("permission");

        if (node == null) {
            permission = null;
            return;
        }

        permission = new Permission(node, PermissionDefault.OP);
    }
}
