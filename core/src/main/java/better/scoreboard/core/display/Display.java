package better.scoreboard.core.display;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.condition.Condition;
import com.github.retrooper.packetevents.protocol.player.User;
import sharkbyte.configuration.core.ConfigSection;

public abstract class Display {

    protected final Condition condition;
    protected final int weight;

    public Display(BetterScoreboard plugin, ConfigSection config) {
        weight = config.getObject(Integer.class, "weight", 1);
        if (config.hasNode("criteria")) condition = new Condition(plugin, config);
        else condition = null;
    }

    public int getWeight() {
        return weight;
    }

    public boolean canRun(User user) {
        if (condition == null) return true;
        return condition.isTrue(user);
    }

    public abstract void tick();
}
