package better.scoreboard.core.listener;

import better.scoreboard.core.displayuser.DisplayUser;
import better.scoreboard.core.displayuser.DisplayUserManager;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketConfigReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;

/**
 * In modern versions of Minecraft, you can transition between the play state and configuration state freely during
 * game. If you send a play state packet during the configuration state, you can lead to the client disconnecting due to
 * improper state management. So, we have to keep account of whether we're allowed to communicate with the client at any
 * given time.
 */
public class StateChangeListener extends SimplePacketListenerAbstract {

    @Override
    public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
        if (event.getPacketType() != PacketType.Configuration.Client.CONFIGURATION_END_ACK) return;
        DisplayUser user = DisplayUserManager.getDisplayUser(event.getUser());
        if (user == null) return;
        user.setState(0);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        if (event.getPacketType() != PacketType.Play.Client.CONFIGURATION_ACK) return;
        DisplayUser user = DisplayUserManager.getDisplayUser(event.getUser());
        if (user == null) return;
        user.setState(1);
    }
}
