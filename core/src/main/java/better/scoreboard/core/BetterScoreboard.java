package better.scoreboard.core;

import better.scoreboard.core.bridge.Data;
import better.scoreboard.core.bridge.PlaceholderProcessor;
import better.scoreboard.core.bridge.PluginLogger;
import better.scoreboard.core.condition.Condition;
import better.scoreboard.core.condition.ConditionManager;
import better.scoreboard.core.configuration.ConfigurationFile;
import better.scoreboard.core.configuration.ConfigurationSection;
import better.scoreboard.core.display.Display;
import better.scoreboard.core.display.DisplayManager;
import better.scoreboard.core.display.impl.BarDisplay;
import better.scoreboard.core.display.impl.BoardDisplay;
import better.scoreboard.core.displayuser.DisplayUser;
import better.scoreboard.core.displayuser.DisplayUserManager;
import better.scoreboard.core.listener.JoinLeaveListener;
import better.scoreboard.core.placeholder.PlaceholderManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import com.github.retrooper.packetevents.event.simple.PacketPlaySendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import sharkbyte.scoreboard.core.SBScoreboard;

import java.nio.file.Path;

public class BetterScoreboard {

    private static ConfigurationFile settings = null;

    private final PlaceholderProcessor placeholders;
    private final PluginLogger logger;
    private final Data data;
    private final Path path;

    private boolean enabled;

    public BetterScoreboard(PlaceholderProcessor placeholders, PluginLogger logger, Data data, Path path) {
        this.placeholders = placeholders;
        this.logger = logger;
        this.data = data;
        this.path = path;
        this.enabled = true;
    }

    /*
     *
     */

    public void init() {
        if (!enabled) return;

        /*
         * Load the ConditionManager.
         */

        // Register numerical operations.
        ConditionManager.registerConditionCheck(">", (leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left > right;
        });
        ConditionManager.registerConditionCheck(">=", ((leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left >= right;
        }));
        ConditionManager.registerConditionCheck("<", ((leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left < right;
        }));
        ConditionManager.registerConditionCheck("<=", ((leftText, rightText) -> {
            double left = Double.parseDouble(leftText), right = Double.parseDouble(rightText);
            return left <= right;
        }));

        // Register string operations.
        ConditionManager.registerConditionCheck("=", String::equalsIgnoreCase);
        ConditionManager.registerConditionCheck("==", String::equals);
        ConditionManager.registerConditionCheck("!=", ((leftText, rightText) -> !leftText.equalsIgnoreCase(rightText)));
        ConditionManager.registerConditionCheck("!==", ((leftText, rightText) -> !leftText.equals(rightText)));
        ConditionManager.registerConditionCheck("|-", ((leftText, rightText) -> leftText.toLowerCase().startsWith(rightText.toLowerCase())));
        ConditionManager.registerConditionCheck("||-", (String::startsWith));
        ConditionManager.registerConditionCheck("-|", ((leftText, rightText) -> leftText.toLowerCase().endsWith(rightText.toLowerCase())));
        ConditionManager.registerConditionCheck("-||", (String::endsWith));
        ConditionManager.registerConditionCheck("$", ((leftText, rightText) -> leftText.toLowerCase().contains(rightText.toLowerCase())));
        ConditionManager.registerConditionCheck("$$", (String::contains));
    }

    public void enable() {
        if (!enabled) return;

        PacketEvents.getAPI().getEventManager().registerListener(new JoinLeaveListener());
        load();
    }

    public void disable() {
        enabled = false;
    }

    public void load() {
        if (!enabled) return;

        if (settings == null) {
            settings = new ConfigurationFile("settings.yml", path, BetterScoreboard.class.getResourceAsStream("/settings.yml"));
        }

        logger.logInfo("Beginning load!");

        ConfigurationSection config = settings.load();
        PlaceholderManager.setDateFormatter(config.getObject(String.class, "date-format", ""));

        // Nuke and rebuild Conditions.
        logger.logInfo("Rebuilding Conditions from config...");
        ConditionManager.clear();
        Condition.load(this);

        // Nuke and rebuild Displays.
        logger.logInfo("Rebuilding Displays from config...");
        for (DisplayUser user : DisplayUserManager.getDisplayUsers()) user.clearDisplays();
        DisplayManager.clear();
        BarDisplay.load(this);
        BoardDisplay.load(this);

        // Register users back to displays.
        logger.logInfo("Rebuilding DisplayUsers...");
        for (DisplayUser user : DisplayUserManager.getDisplayUsers()) user.checkDisplays();

        logger.logInfo("Load finished!");
    }

    public void tick() {
        for (Display display : DisplayManager.getDisplays()) display.tick();
        for (DisplayUser user : DisplayUserManager.getDisplayUsers()) user.tick();
    }

    /*
     * Getters.
     */

    public Data getData() {
        return data;
    }

    public PluginLogger getLogger() {
        return logger;
    }

    public PlaceholderProcessor getPlaceholders() {
        return placeholders;
    }

    public Path getPath() {
        return path;
    }
}
