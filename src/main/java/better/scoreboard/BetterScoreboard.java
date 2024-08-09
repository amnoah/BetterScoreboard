package better.scoreboard;

import better.scoreboard.board.Board;
import better.scoreboard.board.BoardUser;
import better.scoreboard.condition.Condition;
import better.scoreboard.listener.PlayerUpdateListener;
import better.scoreboard.listener.ReloadListener;
import better.scoreboard.manager.BoardManager;
import better.scoreboard.manager.BoardUserManager;
import better.scoreboard.manager.ConditionManager;
import better.scoreboard.manager.TriggerManager;
import better.scoreboard.trigger.impl.*;
import better.scoreboard.util.MessageUtil;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Our main class.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.1.0
 */
public class BetterScoreboard extends JavaPlugin {

    private static final int B_STATS_ID = 22862;

    private Metrics metrics;
    private BukkitTask task;

    /**
     * Register all built-in triggers as early as we can.
     */
    @Override
    public void onLoad() {
        ConditionManager.registerConditionCheck(">", (leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left > right;
        });
        ConditionManager.registerConditionCheck(">=", ((leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left >= right;
        }));
        ConditionManager.registerConditionCheck("<", ((leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left < right;
        }));
        ConditionManager.registerConditionCheck("<=", ((leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left <= right;
        }));
        ConditionManager.registerConditionCheck("=", String::equalsIgnoreCase);
        ConditionManager.registerConditionCheck("==", String::equals);
        ConditionManager.registerConditionCheck("!=", ((leftText, rightText) -> !leftText.equalsIgnoreCase(rightText)));
        ConditionManager.registerConditionCheck("!==", ((leftText, rightText) -> !leftText.equals(rightText)));
        ConditionManager.registerConditionCheck("|-", ((leftText, rightText) -> leftText.toLowerCase().startsWith(rightText.toLowerCase())));
        ConditionManager.registerConditionCheck("||-", (String::startsWith));
        ConditionManager.registerConditionCheck("-|", ((leftText, rightText) -> leftText.toLowerCase().endsWith(rightText.toLowerCase())));
        ConditionManager.registerConditionCheck("-||", (String::endsWith));
        ConditionManager.registerConditionCheck("$", ((leftText, rightText) -> leftText.toLowerCase().contains(rightText.toLowerCase())));
        ConditionManager.registerConditionCheck("$$", (String::contains));

        TriggerManager.registerTrigger("permission", new PermissionTrigger());
        TriggerManager.registerTrigger("world_whitelist", new WorldWhitelistTrigger());
        TriggerManager.registerTrigger("world_blacklist", new WorldBlacklistTrigger());
        TriggerManager.registerTrigger("world_whitelist_and_permission", new PermWorldWhitelistTrigger());
        TriggerManager.registerTrigger("world_blacklist_and_permission", new PermWorldBlacklistTrigger());
    }

    @Override
    public void onEnable() {
        /*
         * We only support 1.20.3+.
         * We could support below with no code changes, but I don't feel like dealing with the limitations of
         * sharkbyte-scoreboard in older versions.
         */
        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_20_3)) {
            getLogger().warning("You are running on an unsupported version of Minecraft!");
            getLogger().warning("Please update to 1.20.3 or above!");
            return;
        }

        // Begin b_stats
        metrics = new Metrics(this, B_STATS_ID);

        // Register all listeners.

        getServer().getPluginManager().registerEvents(new PlayerUpdateListener(), this);

        if (Bukkit.getPluginManager().getPlugin("BetterReload") != null) {
            getServer().getPluginManager().registerEvents(new ReloadListener(this), this);
        }

        // Load everything else.

        MessageUtil.setUsePAPI(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null);
        reload();
        task = getServer().getScheduler().runTaskTimerAsynchronously(this, () -> { // async for timings
            for (Board board : BoardManager.getBoards()) board.tick();
            for (BoardUser user : BoardUserManager.getBoardUsers()) user.tick();
        }, 0, 1);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        metrics.shutdown();
        task.cancel();
        task = null;
    }

    /**
     * When this is called we will rebuild everything from the config!
     */
    public void reload() {
        saveDefaultConfig();
        reloadConfig();

        // Nuke Conditions.
        ConditionManager.clear();

        // Nuke all boards in the system.
        for (BoardUser user : BoardUserManager.getBoardUsers()) user.switchBoard(null);
        BoardManager.clear();

        MessageUtil.setDateFormat(getConfig().getString("settings.date-format"));

        // Rebuild the conditions.
        ConfigurationSection conditions = getConfig().getConfigurationSection("conditions");
        if (conditions != null) {
            for (String condition : conditions.getKeys(false)) {
                ConfigurationSection section = conditions.getConfigurationSection(condition);

                if (section == null) {
                    getLogger().warning("Could not resolve condition named \"" + condition + "\" in config.yml!");
                    continue;
                }

                ConditionManager.addCondition(condition.toLowerCase(), new Condition(section));
            }
        }

        // Rebuild the scoreboards.
        ConfigurationSection scoreboards = getConfig().getConfigurationSection("scoreboards");
        if (scoreboards != null) {
            for (String scoreboard : scoreboards.getKeys(false)) {
                ConfigurationSection section = scoreboards.getConfigurationSection(scoreboard);

                if (section == null) {
                    getLogger().warning("Could not resolve scoreboard named \"" + scoreboard + "\" in config.yml!");
                    continue;
                }

                BoardManager.addBoard(new Board(section));
            }
        }

        // Register users back to boards.
        for (BoardUser user : BoardUserManager.getBoardUsers()) user.checkBoards();
    }
}
