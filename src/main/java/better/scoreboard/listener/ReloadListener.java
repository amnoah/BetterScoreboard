package better.scoreboard.listener;

import better.reload.api.ReloadEvent;
import better.scoreboard.BetterScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * This class listeners for BetterReload's ReloadEvent and initializes a BetterScoreboard reload when it occurs.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class ReloadListener implements Listener {

    private final BetterScoreboard plugin;

    public ReloadListener(BetterScoreboard plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(ReloadEvent event) {
        plugin.reload();
    }
}
