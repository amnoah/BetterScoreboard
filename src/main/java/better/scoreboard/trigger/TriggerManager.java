package better.scoreboard.trigger;

import better.scoreboard.trigger.Trigger;
import better.scoreboard.trigger.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The TriggerManager allows custom triggers to be added by other plugins and still be accessible from our configuration
 * file! During the load stage register your Trigger to the map with the name that should represent it in the config.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class TriggerManager {

    private final static Map<String, Trigger> TRIGGER_MAP = new HashMap<>();

    static {
        registerTrigger("permission", new PermissionTrigger());
        registerTrigger("world_whitelist", new WorldWhitelistTrigger());
        registerTrigger("world_blacklist", new WorldBlacklistTrigger());
        registerTrigger("world_whitelist_and_permission", new PermWorldWhitelistTrigger());
        registerTrigger("world_blacklist_and_permission", new PermWorldBlacklistTrigger());
    }

    /**
     * Register a Trigger to be associated with the given name.
     */
    public static void registerTrigger(String name, Trigger trigger) {
        TRIGGER_MAP.put(name.toLowerCase(), trigger);
    }

    /**
     * Retrieve a copy of the Trigger associated with the given name.
     */
    public static Trigger retrieveTrigger(String name) {
        if (name == null) return new DefaultTrigger();
        Trigger trigger = TRIGGER_MAP.get(name.toLowerCase());
        if (trigger == null) return new DefaultTrigger();
        return trigger.clone();
    }
}
