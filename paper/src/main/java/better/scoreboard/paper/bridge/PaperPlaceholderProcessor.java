package better.scoreboard.paper.bridge;

import better.scoreboard.core.bridge.PlaceholderProcessor;
import better.scoreboard.paper.BetterScoreboardPaper;
import com.github.retrooper.packetevents.protocol.player.User;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;

public class PaperPlaceholderProcessor implements PlaceholderProcessor {

    private final BetterScoreboardPaper plugin;

    public PaperPlaceholderProcessor(BetterScoreboardPaper plugin) {
        this.plugin = plugin;
    }
    @Override
    public String setPlaceholders(User user, String placeholder) {
        if (plugin.isPAPIInstalled()) {
            placeholder = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(user.getUUID()), placeholder);
        }

        return placeholder;
    }
}
