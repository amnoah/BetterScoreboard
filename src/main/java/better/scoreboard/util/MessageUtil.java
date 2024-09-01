package better.scoreboard.util;

import better.scoreboard.condition.ConditionManager;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple utility class for converting messages to client-usable ones.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.1.0
 */
public class MessageUtil {

    // Stolen from TAB.
    private final static Pattern PLACEHOLDER_PATTERN = Pattern.compile("%([^%]*)%");

    /*
     * Built in placeholders:
     *
     * %date% - The current date.
     *
     * %displayname% - The player's current display name.
     * %gamemode% - The player's current gamemode.
     * %health% - The player's current health (rounded).
     * %ping% - The player's current ping.
     * %username% - The player's username.
     * %world% - Current world name.
     *
     * %maxplayers% - Max player count.
     * %players% - Current player count.
     * %worldplayers% - The player count of the player's current world.
     */

    private static DateTimeFormatter dateFormatter;
    private static boolean usePAPI = false;

    /*
     * Setters.
     */

    /**
     * Set the date formatter that should be used.
     */
    public static void setDateFormat(String format) {
        dateFormatter = DateTimeFormatter.ofPattern(format);
    }

    /**
     * Set whether PlaceholderAPI should be used.
     */
    public static void setUsePAPI(boolean usePAPI) {
        MessageUtil.usePAPI = usePAPI;
    }

    /*
     * String modifiers.
     */

    /**
     * Find all placeholders in a given piece of text, separating them into a set.
     */
    public static Set<String> separatePlaceholders(String text) {
        Set<String> separated = new HashSet<>();
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(text);
        while (matcher.find()) separated.add(matcher.group());
        return separated;
    }

    /**
     * Replace a given placeholder with the text it should display.
     */
    public static String setPlaceholder(Player player, String placeholder) {
        switch (placeholder) {
            case "%date%":
                return LocalDateTime.now().format(dateFormatter);
            case "%displayname%":
                return player.getDisplayName();
            case "%gamemode%":
                return player.getGameMode().name();
            case "%health%":
                return String.valueOf((int) Math.round(player.getHealth()));
            case "%ping%":
                return String.valueOf(player.getPing());
            case "%username%":
                return player.getName();
            case "%world%":
                return player.getWorld().getName();
            case "%maxplayers%":
                return String.valueOf(Bukkit.getMaxPlayers());
            case "%players%":
                return String.valueOf(Bukkit.getOnlinePlayers().size());
            case "%worldplayers%":
                return String.valueOf(player.getWorld().getPlayers().size());
            default:
                // Check if it's our condition system.
                if (placeholder.startsWith("%condition:"))
                    return ConditionManager.getCondition(placeholder.substring(11, placeholder.length() - 1)).getText(player);
                // Check if it's placeholderapi.
                if (usePAPI) return PlaceholderAPI.setPlaceholders(player, placeholder);
        }

        return placeholder;
    }

    /**
     * Return a version of the string with all color codes translated into client usable versions.
     */
    public static String translateColors(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
