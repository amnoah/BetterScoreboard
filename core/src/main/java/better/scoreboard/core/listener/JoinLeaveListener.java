package better.scoreboard.core.listener;

import better.scoreboard.core.displayuser.DisplayUser;
import better.scoreboard.core.displayuser.DisplayUserManager;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.event.simple.PacketConfigReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

public class JoinLeaveListener extends SimplePacketListenerAbstract {

    @Override
    public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
        if (event.getPacketType() != PacketType.Configuration.Client.CONFIGURATION_END_ACK) return;
        DisplayUser user = DisplayUserManager.getDisplayUser(event.getUser());
        if (user == null) return;
        user.checkDisplays();
    }

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
