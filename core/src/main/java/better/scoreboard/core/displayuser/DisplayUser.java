package better.scoreboard.core.displayuser;

import better.scoreboard.core.processor.Processor;
import better.scoreboard.core.processor.impl.BarProcessor;
import better.scoreboard.core.processor.impl.BoardProcessor;
import com.github.retrooper.packetevents.protocol.player.User;

import java.util.ArrayList;
import java.util.List;

public class DisplayUser {

    private final List<Processor> processors = new ArrayList<>();

    // 0 = Play, 1 = Configuration.
    private int state = 0;

    public DisplayUser(User user) {
        processors.add(new BarProcessor(user));
        processors.add(new BoardProcessor(user));
        checkDisplays();
    }

    /*
     * Setter.
     */

    public void setState(int state) {
        this.state = state;
        // If we transition back to a play state, re-assemble boards.
        if (state == 0) {
            checkDisplays();
        }
    }

    /*
     * Functions.
     */

    @SuppressWarnings("all")
    public void checkDisplays() {
        if (state != 0) return;
        for (Processor processor : processors) processor.checkDisplays();
    }

    @SuppressWarnings("all")
    public void clearDisplays() {
        if (state != 0) return;
        for (Processor processor : processors) processor.switchDisplay(null);
    }

    @SuppressWarnings("all")
    public void tick() {
        if (state != 0) return;
        for (Processor processor : processors) processor.tick();
    }
}
