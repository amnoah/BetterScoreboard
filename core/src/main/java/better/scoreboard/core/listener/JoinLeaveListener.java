package better.scoreboard.core.listener;

import better.scoreboard.core.displayuser.DisplayUserManager;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class JoinLeaveListener extends SimplePacketListenerAbstract {

    @Override
    public void onPacketPlaySend(PacketPlaySendEvent event) {
        if (event.getPacketType() != PacketType.Play.Server.JOIN_GAME) return;
        DisplayUserManager.addDisplayUser(event.getUser());
    }

    @Override
    public void onUserDisconnect(UserDisconnectEvent event) {
        DisplayUserManager.removeDisplayUser(event.getUser());
    }
}
