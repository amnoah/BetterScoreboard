package better.scoreboard.manager;

import better.scoreboard.board.Board;
import sharkbyte.universal.data.ArraySet;

import java.util.Set;

/**
 * This is a simple tracker for all active Boards.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class BoardManager {

    private final static Set<Board> BOARDS = new ArraySet<>();

    /*
     * Getters.
     */

    /**
     * Return all active Boards.
     */
    public static Set<Board> getBoards() {
        return BOARDS;
    }

    /*
     * Set modifications.
     */

    /**
     * Add a Board to the tracking set.
     */
    public static void addBoard(Board board) {
        BOARDS.add(board);
    }

    /**
     * Remove all Board objects from the tracking set.
     * NOTE: This will NOT remove the boards from inside BoardUser objects.
     */
    public static void clear() {
        BOARDS.clear();
    }

    /**
     * Remove the inputted Board from the tracking set.
     * NOTE: This will NOT remove the board from inside BoardUser objects.
     */
    public static void removeBoard(Board board) {
        BOARDS.remove(board);
    }
}
