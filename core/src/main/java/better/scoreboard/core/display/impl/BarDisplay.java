package better.scoreboard.core.display.impl;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.animation.impl.ColorAnimation;
import better.scoreboard.core.animation.impl.DivisionAnimation;
import better.scoreboard.core.animation.impl.HealthAnimation;
import better.scoreboard.core.animation.impl.TextAnimation;
import better.scoreboard.core.configuration.ConfigurationFile;
import better.scoreboard.core.configuration.ConfigurationSection;
import better.scoreboard.core.display.Display;
import better.scoreboard.core.display.DisplayManager;

public class BarDisplay extends Display {

    private static ConfigurationFile bossBars = null;

    private final ColorAnimation color;
    private final DivisionAnimation division;
    private final HealthAnimation health;
    private final TextAnimation text;

    public BarDisplay(BetterScoreboard plugin, ConfigurationSection config) {
        super(plugin, config);
        color = new ColorAnimation(plugin, config.getConfigSection("color"));
        division = new DivisionAnimation(plugin, config.getConfigSection("division"));
        health = new HealthAnimation(plugin, config.getConfigSection("health"));
        text = new TextAnimation(plugin, config.getConfigSection("text"));
    }

    public ColorAnimation getColor() {
        return color;
    }

    public DivisionAnimation getDivision() {
        return division;
    }

    public HealthAnimation getHealth() {
        return health;
    }

    public TextAnimation getText() {
        return text;
    }

    public static void load(BetterScoreboard plugin) {
        if (bossBars == null) {
            bossBars = new ConfigurationFile("boss-bars.yml", plugin.getPath(), BetterScoreboard.class.getResourceAsStream("/boss-bars.yml"));
        }

        ConfigurationSection config = bossBars.load();
        for (ConfigurationSection bossBar : config.getChildren()) {
            if (bossBar == null) continue;
            DisplayManager.addDisplay(new BarDisplay(plugin, bossBar));
        }
    }

    @Override
    public void tick() {
        color.tick();
        division.tick();
        health.tick();
        text.tick();
    }
}
