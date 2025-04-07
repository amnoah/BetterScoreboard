package better.scoreboard.spigot.listener;

import better.scoreboard.core.displayuser.DisplayUserManager;
import com.github.retrooper.packetevents.PacketEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerUpdateListener implements Listener {

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        DisplayUserManager.getDisplayUser(PacketEvents.getAPI().getPlayerManager().getUser(event.getPlayer())).checkDisplays();
    }
}
