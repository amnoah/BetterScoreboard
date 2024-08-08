package better.scoreboard.util;

import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Set;

/**
 *
 *
 * @Author: am noah
 * @Since: 1.1.0
 * @Updated: 1.1.0
 */
public class Line {

    private final String text;
    private final Set<String> placeholders;

    public Line(String text) {
        if (text == null) {
            this.text = "";
            placeholders = Collections.emptySet();
            return;
        }

        this.text = text;
        placeholders = MessageUtil.separatePlaceholders(text);
    }

    public String getText(Player player) {
        String text = this.text;
        for (String placeholder : placeholders) text = text.replaceAll(placeholder, MessageUtil.setPlaceholder(player, placeholder));
        return text;
    }
}
