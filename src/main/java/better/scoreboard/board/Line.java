package better.scoreboard.board;

import better.scoreboard.util.MessageUtil;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;

/**
 * This class represents a specific line on the scoreboard.
 * Its primary purpose is to improve placeholder efficiency, separating them on creation so all placeholders are known
 * and we don't have to blindly guess present placeholders each tick.
 *
 * @Author: am noah
 * @Since: 1.1.0
 * @Updated: 1.3.0
 */
public class Line {

    private final String text;
    private final Set<String> placeholders;

    /**
     * Initialize the Line object.
     */
    public Line(@Nullable String text) {
        if (text == null) {
            this.text = null;
            placeholders = Collections.emptySet();
            return;
        }

        this.text = text;
        placeholders = MessageUtil.separatePlaceholders(text);
    }

    /**
     * Return the line's text, replacing all placeholders with what's applicable for the given player.
     */
    public @Nullable String getText(Player player) {
        String text = this.text;
        for (String placeholder : placeholders) text = text.replaceAll(placeholder, MessageUtil.setPlaceholder(player, placeholder));
        return text;
    }
}
