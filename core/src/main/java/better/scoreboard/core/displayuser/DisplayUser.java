package better.scoreboard.core.displayuser;

import better.scoreboard.core.processor.Processor;
import better.scoreboard.core.processor.impl.BarProcessor;
import better.scoreboard.core.processor.impl.BoardProcessor;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;

import java.util.ArrayList;
import java.util.List;

public class DisplayUser {

    private final List<Processor> processors = new ArrayList<>();
    private final User user;

    public DisplayUser(User user) {
        this.user = user;
        processors.add(new BarProcessor(user));
        processors.add(new BoardProcessor(user));
        checkDisplays();
    }

    /*
     * Functions.
     */

    @SuppressWarnings("all")
    public void checkDisplays() {
        if (user.getConnectionState() != ConnectionState.PLAY) return;
        for (Processor processor : processors) processor.checkDisplays();
    }

    @SuppressWarnings("all")
    public void clearDisplays() {
        if (user.getConnectionState() != ConnectionState.PLAY) return;
        for (Processor processor : processors) processor.switchDisplay(null);
    }

    @SuppressWarnings("all")
    public void tick() {
        if (user.getConnectionState() != ConnectionState.PLAY) return;
        for (Processor processor : processors) processor.tick();
    }
}
