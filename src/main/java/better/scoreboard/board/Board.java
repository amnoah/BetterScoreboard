package better.scoreboard.board;

import better.scoreboard.manager.TriggerManager;
import better.scoreboard.trigger.Trigger;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This represents an entire scoreboard. It holds information about the board and all of its text.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class Board {

    private final boolean hideNumbers;
    private final List<Animation> lines;
    private final Animation title;
    private final Trigger trigger;
    private final int weight;

    /**
     * Initialize the Board, reading required data from the configuration.
     */
    public Board(ConfigurationSection config) {
        lines = new ArrayList<>();

        weight = config.getInt("weight", 0);
        hideNumbers = config.getBoolean("hide-numbers", true);
        trigger = TriggerManager.retrieveTrigger(config.getString("trigger"));
        trigger.load(config);

        title = new Animation(config.getConfigurationSection("title"));

        for (int i = 1; i <= 15; i++) {
            ConfigurationSection section = config.getConfigurationSection("line" + i);
            if (section == null) break;
            lines.add(new Animation(section));
        }
    }

    /*
     * Getters.
     */

    /**
     * Return the text that should be displayed at the given index.
     */
    public Animation getLine(int index) {
        return lines.get(index);
    }

    /**
     * Return the total amount of lines on this board.
     */
    public int getLineCount() {
        return lines.size();
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

    /**
     * Return whether numbers should be hidden on this board.
     */
    public boolean shouldHideNumbers() {
        return hideNumbers;
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
        for (Animation line : lines) line.tick();
    }
}
