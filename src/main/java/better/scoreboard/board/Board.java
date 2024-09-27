package better.scoreboard.board;

import better.scoreboard.BetterScoreboard;
import better.scoreboard.trigger.Trigger;
import better.scoreboard.trigger.TriggerManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents an entire scoreboard. It holds information about the board and all of its text.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.3.0
 */
public class Board {

    private final List<Animation> leftAligned, rightAligned;
    private final Animation title;
    private final Trigger trigger;
    private final int weight;

    /**
     * Initialize the Board, reading required data from the configuration.
     */
    public Board(BetterScoreboard plugin, ConfigurationSection config) {
        leftAligned = new ArrayList<>();
        rightAligned = new ArrayList<>();

        weight = config.getInt("weight", 0);
        trigger = TriggerManager.retrieveTrigger(config.getString("trigger"));
        trigger.load(config);

        title = new Animation(plugin, config.getConfigurationSection("title"));

        for (int i = 1; i <= 15; i++) {
            ConfigurationSection section = config.getConfigurationSection("line" + i);
            if (section == null) break;
            leftAligned.add(new Animation(plugin, section.getConfigurationSection("left-aligned")));
            rightAligned.add(new Animation(plugin, section.getConfigurationSection("right-aligned")));
        }
    }

    /*
     * Getters.
     */

    /**
     * Return the text that should be displayed at the given index.
     */
    public Animation getLeftText(int index) {
        return leftAligned.get(index);
    }

    /**
     * Return the total amount of lines on this board.
     */
    public int getLineCount() {
        return leftAligned.size();
    }

    public Animation getRightText(int index) {
        return rightAligned.get(index);
    }

    /**
     * Return the text that should be displayed as the title.
     */
    public Animation getTitle() {
        return title;
    }

    /**
     * Return the board's weight.
     */
    public int getWeight() {
        return weight;
    }

    /*
     * Functions.
     */

    /**
     * Return whether the player is allowed to run this scoreboard.
     */
    public boolean canRun(Player player) {
        return trigger.canRun(player);
    }

    /**
     * Progress this scoreboard 1 tick forward.
     */
    public void tick() {
        title.tick();
        for (Animation line : leftAligned) line.tick();
        for (Animation line : rightAligned) line.tick();
    }
}
