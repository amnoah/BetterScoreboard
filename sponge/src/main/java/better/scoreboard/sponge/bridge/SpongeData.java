package better.scoreboard.sponge.bridge;

import better.scoreboard.core.bridge.Data;
import com.github.retrooper.packetevents.protocol.player.User;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import sharkbyte.configuration.configurate.ConfigurateConfigationFile;
import sharkbyte.configuration.core.ConfigurationFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class SpongeData implements Data {

    private final Game game;

    public SpongeData(Game game) {
        this.game = game;
    }

    @Override
    public boolean hasPermission(User user, String... permission) {
        Optional<ServerPlayer> player = game.server().player(user.getUUID());
        if (player.isEmpty()) return false;
        for (String p : permission) if (!player.get().hasPermission(p)) return false;
        return true;
    }
}
