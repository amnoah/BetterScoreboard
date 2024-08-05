package better.scoreboard.trigger.impl;

import better.scoreboard.trigger.Trigger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * This trigger will be used when no others can be resolved.
 * This trigger will assume that everyone can use the scoreboard.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class DefaultTrigger extends Trigger {

    /**
     * Any player can run this trigger.
     */
    @Override
    public boolean canRun(Player player) {
        return true;
    }

    /**
     * Nothing is loaded by this trigger.
     */
    @Override
    public void load(ConfigurationSection config) {
        // Unused.
    }
}
