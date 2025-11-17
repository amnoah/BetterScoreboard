package better.scoreboard.core.animation;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.condition.Condition;
import better.scoreboard.core.configuration.ConfigurationSection;
import com.github.retrooper.packetevents.protocol.player.User;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Animation<E> {

    protected final List<E> animation = new ArrayList<>();
    protected final int animationSpeed;
    protected final boolean random;
    protected final Condition condition;

    protected int currentIndex, currentTick;
    protected boolean updateTick;

    public Animation(BetterScoreboard plugin, @Nullable ConfigurationSection config) {
        currentIndex = currentTick = 0;

        if (config == null) {
            animationSpeed = -1;
            random = false;
            condition = null;
            return;
        }

        random = config.getObject(Boolean.class, "random", false);
        animationSpeed = config.getObject(Integer.class, "speed", 1);

        if (animationSpeed < 0) updateTick = true;

        if (config.hasNode("criteria")) condition = new Condition(plugin, config);
        else condition = null;
    }

    public E getAnimation() {
        return animation.get(currentIndex);
    }

    public boolean isConditionalTrue(User user) {
        if (condition == null) return true;
        return condition.isTrue(user);
    }

    public boolean isUpdateTick() {
        return updateTick;
    }

    public void tick() {
        // Should not proceed if it's a static board.
        if (animationSpeed < 0) return;

        // Only progress further once the animation's refresh rate has elapsed.
        currentTick++;
        updateTick = false;
        if (currentTick < animationSpeed) return;
        currentTick = 0;
        updateTick = true;

        // Randomly select an index if that's chosen in the config.
        if (random) {
            if (animation.size() <= 1) return;
            int nextIndex = currentIndex;
            while (currentIndex == nextIndex) {
                nextIndex = (int) (Math.random() * animation.size());
            }
            currentIndex = nextIndex;
            return;
        }

        // Otherwise progress the current index.
        currentIndex++;
        if (currentIndex == animation.size()) currentIndex = 0;
    }
}
