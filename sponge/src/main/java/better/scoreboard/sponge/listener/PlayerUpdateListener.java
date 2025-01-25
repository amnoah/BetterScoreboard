package better.scoreboard.sponge.listener;

import better.scoreboard.core.displayuser.DisplayUserManager;
import com.github.retrooper.packetevents.PacketEvents;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ServerSideConnectionEvent;

public class PlayerUpdateListener {

    @Listener
    public void onWorldChange(ChangeEntityWorldEvent event) {
        if (!(event.entity() instanceof ServerPlayer player)) return;
        DisplayUserManager.getDisplayUser(PacketEvents.getAPI().getPlayerManager().getUser(player)).checkDisplays();
    }
}
