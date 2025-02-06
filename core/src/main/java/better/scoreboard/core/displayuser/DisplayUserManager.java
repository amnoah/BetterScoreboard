package better.scoreboard.core.displayuser;

import com.github.retrooper.packetevents.protocol.player.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DisplayUserManager {

    private final static Map<User, DisplayUser> DISPLAY_USER_MAP = new HashMap<>();

    /*
     * Getters.
     */

    /**
     * Return the DisplayUser associated with the given player.
     */
    public static DisplayUser getDisplayUser(User user) {
        return DISPLAY_USER_MAP.get(user);
    }

    /**
     * Return all DisplayUsers.
     */
    public static Collection<DisplayUser> getDisplayUsers() {
        return DISPLAY_USER_MAP.values();
    }

    /*
     * Map modifications.
     */

    /**
     * Add a DisplayUser associated with the given player object.
     */
    public static void addDisplayUser(User user) {
        DISPLAY_USER_MAP.put(user, new DisplayUser(user));
    }

    /**
     * Remove the DisplayUser associated with the given player object.
     */
    public static void removeDisplayUser(User user) {
        DISPLAY_USER_MAP.remove(user);
    }
}
