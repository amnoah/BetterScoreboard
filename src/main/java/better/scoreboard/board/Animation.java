package better.scoreboard.board;

import better.scoreboard.BetterScoreboard;
import better.scoreboard.condition.Condition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This represents a single line on the scoreboard. It can cycle through multiple lines of text and randomly select
 * a line if set to.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.3.0
 */
public class Animation {

    private final List<Line> animation;
    private final int animationSpeed;
    private final boolean random;
    private final Condition condition;

    private int currentIndex, currentTick;
    private boolean updateTick;

    /**
     * Initialize the Animation, reading required data from the configuration.
     */
    public Animation(BetterScoreboard plugin, @Nullable ConfigurationSection config) {
        currentIndex = currentTick = 0;

        if (config == null) {
            animationSpeed = -1;
            random = false;
            animation = Collections.singletonList(new Line(null));
            condition = null;
            return;
        }

        random = config.getBoolean("random", false);
        animationSpeed = config.getInt("speed", 1);
        animation = new ArrayList<>();
        for (String line : config.getStringList("text")) animation.add(new Line(line));

        if (random) currentIndex = (int) (animation.size() * Math.random());
        if (animationSpeed < 0) updateTick = true;

        if (config.get("criteria") != null) condition = new Condition(plugin, config);
        else condition = null;
    }

    /*
     * Getters.
     */

    /**
     * Returns the current string of text that should be displayed by this animation.
     */
    public String getText(Player player) {
        return animation.get(currentIndex).getText(player);
    }

    public boolean isConditionalTrue(Player player) {
        if (condition == null) return true;
        return condition.isTrue(player);
    }

    /**
     * Return whether the current tick is an animation update tick.
     */
    public boolean isUpdateTick() {
        return updateTick;
    }

    /*
     * Functions.
     */

    /**
     * Progresses the animation forward by 1 tick.
     */
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
            currentIndex = (int) (Math.random() * animation.size());
            return;
        }

        // Otherwise progress the current index.
        currentIndex++;
        if (currentIndex == animation.size()) currentIndex = 0;
    }
}
