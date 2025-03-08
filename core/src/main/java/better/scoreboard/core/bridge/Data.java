package better.scoreboard.core.bridge;

import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.configuration.core.ConfigurationFile;

import java.io.InputStream;

public interface Data {

    ConfigurationFile getConfigurationFile(String name, InputStream defaultFile);

    boolean hasPermission(User user, String... permission);
}
