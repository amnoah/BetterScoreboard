package better.scoreboard.paper.bridge;

import better.scoreboard.core.bridge.PluginLogger;
import better.scoreboard.paper.BetterScoreboardPaper;

public class PaperPluginLogger implements PluginLogger {

    private final BetterScoreboardPaper plugin;

    public PaperPluginLogger(BetterScoreboardPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public void logInfo(String message) {
        plugin.getLogger().info(message);
    }

    @Override
    public void logWarning(String message) {
        plugin.getLogger().warning(message);
    }

}
