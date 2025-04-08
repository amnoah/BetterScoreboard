package better.scoreboard.paper.listener;

import better.reload.api.ReloadEvent;
import better.scoreboard.paper.BetterScoreboardPaper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReloadListener implements Listener {

    private final BetterScoreboardPaper plugin;

    public ReloadListener(BetterScoreboardPaper plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onReload(ReloadEvent event) {
        plugin.getCore().load();
    }
}
