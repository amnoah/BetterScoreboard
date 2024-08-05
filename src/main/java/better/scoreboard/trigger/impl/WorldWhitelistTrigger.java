package better.scoreboard.trigger.impl;

import better.scoreboard.trigger.Trigger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This trigger only allow players in the configured worlds to use the scoreboard.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class WorldWhitelistTrigger extends Trigger {

    private List<String> worlds = null;

    /**
     * The player can run this trigger if they're in one of the configured worlds.
     */
    @Override
    public boolean canRun(Player player) {
        for (String world : worlds) if (player.getWorld().getName().equalsIgnoreCase(world)) return true;
        return false;
    }

    /**
     * Reload this trigger, grabbing the worlds from the configuration.
     */
    @Override
    public void load(ConfigurationSection config) {
        worlds = config.getStringList("worlds");
    }
}
