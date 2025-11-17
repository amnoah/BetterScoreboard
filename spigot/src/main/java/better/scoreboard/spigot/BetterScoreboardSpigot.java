package better.scoreboard.spigot;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.placeholder.PlaceholderManager;
import better.scoreboard.spigot.bridge.SpigotData;
import better.scoreboard.spigot.bridge.SpigotPlaceholderProcessor;
import better.scoreboard.spigot.bridge.SpigotPluginLogger;
import better.scoreboard.spigot.listener.PlayerUpdateListener;
import better.scoreboard.spigot.listener.ReloadListener;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import sharkbyte.bossbar.core.legacy.LegacyBossBar;
import sharkbyte.bossbar.core.legacy.LegacyHandler;

public class BetterScoreboardSpigot extends JavaPlugin {

    private static final int B_STATS_ID = 22862;

    // Core objects.
    private BetterScoreboard core;

    // Bukkit objects.
    private BukkitTask task;
    private Metrics metrics;

    private boolean papiInstalled;

    @Override
    public void onLoad() {
        core = new BetterScoreboard(
                new SpigotPlaceholderProcessor(this),
                new SpigotPluginLogger(this),
                new SpigotData(this),
                getDataFolder().toPath()
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

        // Begin bStats.
        metrics = new Metrics(this, B_STATS_ID);

        // Check if PAPI is installed.
        papiInstalled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

        // Register all listeners.
        getServer().getPluginManager().registerEvents(new PlayerUpdateListener(), this);
        if (getServer().getPluginManager().getPlugin("BetterReload") != null) {
            getServer().getPluginManager().registerEvents(new ReloadListener(this), this);
        }

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> core.tick(), 0, 1);

        LegacyHandler.setEntityIDProvider(SpigotReflectionUtil::generateEntityId);
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
