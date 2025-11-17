package better.scoreboard.paper.bridge;

import better.scoreboard.core.bridge.Data;
import better.scoreboard.paper.BetterScoreboardPaper;
import com.github.retrooper.packetevents.protocol.player.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import sharkbyte.configuration.core.ConfigurationFile;
import sharkbyte.configuration.spigot.SpigotConfigurationFile;

import java.io.InputStream;

public class PaperData implements Data {

    private final BetterScoreboardPaper plugin;

    public PaperData(BetterScoreboardPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean hasPermission(User user, String... permission) {
        Player player = Bukkit.getPlayer(user.getUUID());
        if (player == null) return false;
        if (player.isOp()) return true;

        for (String string : permission) {
            if (player.hasPermission(string)) return true;
        }

        return false;
    }
}
