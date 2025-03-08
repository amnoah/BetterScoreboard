package better.scoreboard.spigot.listener;

import better.reload.api.ReloadEvent;
import better.scoreboard.spigot.BetterScoreboardSpigot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReloadListener implements Listener {

    private final BetterScoreboardSpigot plugin;

    public ReloadListener(BetterScoreboardSpigot plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(ReloadEvent event) {
        plugin.getCore().load();
    }
}
