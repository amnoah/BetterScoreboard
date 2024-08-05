package better.scoreboard.manager;

import better.scoreboard.board.BoardUser;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BoardUserManager {

    private final static Map<Player, BoardUser> BOARD_USER_MAP = new HashMap<>();

    /*
     * Getters.
     */

    public static BoardUser getBoardUser(Player player) {
        return BOARD_USER_MAP.get(player);
    }

    public static Collection<BoardUser> getBoardUsers() {
        return BOARD_USER_MAP.values();
    }

    /*
     * Map modifications.
     */

    public static void addBoardUser(Player player) {
        BOARD_USER_MAP.put(player, new BoardUser(player));
    }

    public static void removeBoardUser(Player player) {
        BOARD_USER_MAP.remove(player);
    }
}
