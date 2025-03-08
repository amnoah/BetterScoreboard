package better.scoreboard.sponge.listener;

import better.scoreboard.sponge.BetterScoreboardSponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.RefreshGameEvent;

public class ReloadListener {

    private final BetterScoreboardSponge plugin;

    public ReloadListener(BetterScoreboardSponge plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void onReload(final RefreshGameEvent event) {
        plugin.getCore().load();
    }
}
