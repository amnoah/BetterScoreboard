package better.scoreboard.core;

import better.scoreboard.core.bridge.ConfigSection;
import better.scoreboard.core.bridge.PlaceholderProcessor;
import better.scoreboard.core.bridge.PluginLogger;
import better.scoreboard.core.bridge.UserData;
import better.scoreboard.core.condition.Condition;
import better.scoreboard.core.condition.ConditionManager;
import better.scoreboard.core.display.Display;
import better.scoreboard.core.display.DisplayManager;
import better.scoreboard.core.display.impl.BarDisplay;
import better.scoreboard.core.display.impl.BoardDisplay;
import better.scoreboard.core.displayuser.DisplayUser;
import better.scoreboard.core.displayuser.DisplayUserManager;
import better.scoreboard.core.listener.JoinLeaveListener;
import better.scoreboard.core.placeholder.PlaceholderManager;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;

public class BetterScoreboard {

    private final PlaceholderProcessor placeholders;
    private final PluginLogger logger;
    private final UserData data;

    private boolean enabled;

    public BetterScoreboard(PlaceholderProcessor placeholders, PluginLogger logger, UserData data) {
        this.placeholders = placeholders;
        this.logger = logger;
        this.data = data;
        this.enabled = true;

        /*
         * We only support 1.20.3+.
         * We could support below with no code changes, but I don't feel like dealing with the limitations of
         * sharkbyte-scoreboard in older versions.
         */
        if (PacketEvents.getAPI().getServerManager().getVersion().isOlderThan(ServerVersion.V_1_20_3)) {
            logger.logWarning("You are running on an unsupported version of Minecraft!");
            logger.logWarning("Please update to 1.20.3 or above!");
            enabled = false;
        }
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
    }

    public void disable() {
        enabled = false;
    }

    public void load(ConfigSection rootConfig) {
        if (!enabled) return;

        logger.logInfo("Beginning load!");

        PlaceholderManager.setDateFormatter(rootConfig.getConfigSection("settings").getObject(String.class, "date-format", ""));

        // Nuke and rebuild Conditions.
        logger.logInfo("Rebuilding Conditions from config...");
        ConditionManager.clear();
        Condition.load(this, rootConfig);

        // Nuke and rebuild Displays.
        logger.logInfo("Rebuilding Displays from config...");
        for (DisplayUser user : DisplayUserManager.getDisplayUsers()) user.clearDisplays();
        DisplayManager.clear();
        BarDisplay.load(this, rootConfig);
        BoardDisplay.load(this, rootConfig);

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

    public UserData getData() {
        return data;
    }

    public PluginLogger getLogger() {
        return logger;
    }

    public PlaceholderProcessor getPlaceholders() {
        return placeholders;
    }
}
