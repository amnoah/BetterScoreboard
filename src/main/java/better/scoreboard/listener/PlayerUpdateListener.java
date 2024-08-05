package better.scoreboard.listener;

import better.scoreboard.manager.BoardUserManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Simple listener used to either add players to our BoardUser map or check what board they can run.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class PlayerUpdateListener implements Listener {

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        BoardUserManager.getBoardUser(event.getPlayer()).checkBoards();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BoardUserManager.addBoardUser(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        BoardUserManager.removeBoardUser(event.getPlayer());
    }
}
