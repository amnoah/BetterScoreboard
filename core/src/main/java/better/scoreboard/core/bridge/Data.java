package better.scoreboard.core.bridge;

import com.github.retrooper.packetevents.protocol.player.User;

public interface Data {

    boolean hasPermission(User user, String... permission);
}
