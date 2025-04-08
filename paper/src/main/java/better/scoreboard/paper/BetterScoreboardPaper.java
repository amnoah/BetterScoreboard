package better.scoreboard.paper;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.placeholder.PlaceholderManager;
import better.scoreboard.paper.bridge.PaperData;
import better.scoreboard.paper.bridge.PaperPlaceholderProcessor;
import better.scoreboard.paper.bridge.PaperPluginLogger;
import better.scoreboard.paper.listener.PlayerUpdateListener;
import better.scoreboard.paper.listener.ReloadListener;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.folialib.wrapper.task.WrappedTask;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class BetterScoreboardPaper extends JavaPlugin {

    private static final int B_STATS_ID = 22862;

    // Core objects.
    private BetterScoreboard core;

    // Bukkit objects.
    private WrappedTask task;
    private Metrics metrics;

    private boolean papiInstalled;

    @Override
    public void onLoad() {
        core = new BetterScoreboard(
                new PaperPlaceholderProcessor(this),
                new PaperPluginLogger(this),
                new PaperData(this)
        );

        core.init();

        PlaceholderManager.registerPlaceholder("displayname", user -> {
            Player player = Bukkit.getPlayer(user.getUUID());
            return player == null ? "" : player.getDisplayName();
        });
        PlaceholderManager.registerPlaceholder("gamemode", user -> {
            Player player = Bukkit.getPlayer(user.getUUID());
            return player == null ? "" : player.getGameMode().name();
        });
        PlaceholderManager.registerPlaceholder("health", user -> {
            Player player = Bukkit.getPlayer(user.getUUID());
            return player == null ? "" : String.valueOf(player.getHealth());
        });
        PlaceholderManager.registerPlaceholder("maxplayers", user -> String.valueOf(Bukkit.getMaxPlayers()));
        PlaceholderManager.registerPlaceholder("ping", user -> {
            Player player = Bukkit.getPlayer(user.getUUID());
            return player == null ? "" : String.valueOf(player.getPing());
        });
        PlaceholderManager.registerPlaceholder("players", user -> String.valueOf(Bukkit.getOnlinePlayers().size()));
        PlaceholderManager.registerPlaceholder("world", user -> {
            Player player = Bukkit.getPlayer(user.getUUID());
            return player == null ? "" : player.getWorld().getName();
        });
        PlaceholderManager.registerPlaceholder("worldplayers", user -> {
            Player player = Bukkit.getPlayer(user.getUUID());
            return player == null ? "" : String.valueOf(player.getWorld().getPlayers().size());
        });
    }

    @Override
    public void onEnable() {
        core.enable();
        FoliaLib foliaLib = new FoliaLib(this);

        // Begin bStats.
        metrics = new Metrics(this, B_STATS_ID);

        // Check if PAPI is installed.
        papiInstalled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        // Register all listeners.
        getServer().getPluginManager().registerEvents(new PlayerUpdateListener(), this);
        if (getServer().getPluginManager().getPlugin("BetterReload") != null) {
            getServer().getPluginManager().registerEvents(new ReloadListener(this), this);
        }

        task = foliaLib.getScheduler().runTimerAsync(() -> core.tick(), 0, 1);
    }

    @Override
    public void onDisable() {
        core.disable();

        HandlerList.unregisterAll(this);
        metrics.shutdown();
        task.cancel();
        task = null;
    }

    public BetterScoreboard getCore() {
        return core;
    }

    public boolean isPAPIInstalled() {
        return papiInstalled;
    }
}
