package better.scoreboard.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple utility class for converting messages to client-usable ones.
 *
 * @Author: am noah
 * @Since: 1.0.0
 * @Updated: 1.0.0
 */
public class MessageUtil {

    /*
     * Built in placeholders:
     *
     * %date% - The current date.
     *
     * %displayname% - The player's current display name.
     * %gamemode% - The player's current gamemode.
     * %health% - The player's current health.
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
     * Modify the given string to apply all placeholders and convert color codes.
     */
    public static String modify(Player player, String string) {
        // Set built-in placeholders.
        string = string.replaceAll("%date%", LocalDateTime.now().format(dateFormatter));

        string = string.replaceAll("%displayname%", player.getDisplayName());
        string = string.replaceAll("%gamemode%", player.getGameMode().name());
        string = string.replaceAll("%health%", String.valueOf(player.getHealth()));
        string = string.replaceAll("%ping%", String.valueOf(player.getPing()));
        string = string.replaceAll("%username%", player.getName());
        string = string.replaceAll("%world%", player.getWorld().getName());

        string = string.replaceAll("%maxplayers%", String.valueOf(Bukkit.getMaxPlayers()));
        string = string.replaceAll("%players", String.valueOf(Bukkit.getOnlinePlayers().size()));
        string = string.replaceAll("%worldplayers%", String.valueOf(player.getWorld().getPlayers().size()));

        // Set PAPI placeholders.
        if (usePAPI) string = PlaceholderAPI.setPlaceholders(player, string);

        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
