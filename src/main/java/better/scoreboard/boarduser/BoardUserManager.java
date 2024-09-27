package better.scoreboard.boarduser;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple tracker for all BoardUsers.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class BoardUserManager {

    private final static Map<Player, BoardUser> BOARD_USER_MAP = new HashMap<>();

    /*
     * Getters.
     */

    /**
     * Return the BoardUser associated with the given player.
     */
    public static BoardUser getBoardUser(Player player) {
        return BOARD_USER_MAP.get(player);
    }

    /**
     * Return all BoardUsers.
     */
    public static Collection<BoardUser> getBoardUsers() {
        return BOARD_USER_MAP.values();
    }

    /*
     * Map modifications.
     */

    /**
     * Add a BoardUser associated with the given player object.
     */
    public static void addBoardUser(Player player) {
        BOARD_USER_MAP.put(player, new BoardUser(player));
    }

    /**
     * Remove the BoardUser associated with the given player object.
     */
    public static void removeBoardUser(Player player) {
        BOARD_USER_MAP.remove(player);
    }
}
