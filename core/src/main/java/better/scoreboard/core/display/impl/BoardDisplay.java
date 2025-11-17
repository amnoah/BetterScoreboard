package better.scoreboard.core.display.impl;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.animation.impl.TextAnimation;
import better.scoreboard.core.configuration.ConfigurationFile;
import better.scoreboard.core.configuration.ConfigurationSection;
import better.scoreboard.core.display.Display;
import better.scoreboard.core.display.DisplayManager;

import java.util.ArrayList;
import java.util.List;

public class BoardDisplay extends Display {

    private static ConfigurationFile scoreboards = null;

    private final List<TextAnimation> leftAligned, rightAligned;
    private final TextAnimation title;

    public BoardDisplay(BetterScoreboard plugin, ConfigurationSection config) {
        super(plugin, config);

        leftAligned = new ArrayList<>();
        rightAligned = new ArrayList<>();

        title = new TextAnimation(plugin, config.getConfigSection("title"));

        for (int i = 1; i <= 15; i++) {
            ConfigurationSection section = config.getConfigSection("line" + i);
            if (section == null) break;
            leftAligned.add(new TextAnimation(plugin, section.getConfigSection("left-aligned")));
            rightAligned.add(new TextAnimation(plugin, section.getConfigSection("right-aligned")));
        }
    }

    public TextAnimation getLeftText(int index) {
        return leftAligned.get(index);
    }

    public int getLineCount() {
        return leftAligned.size();
    }

    public TextAnimation getRightText(int index) {
        return rightAligned.get(index);
    }

    public TextAnimation getTitle() {
        return title;
    }

    public static void load(BetterScoreboard plugin) {
        if (scoreboards == null) {
            scoreboards = new ConfigurationFile("scoreboards.yml", plugin.getPath(), BetterScoreboard.class.getResourceAsStream("/scoreboards.yml"));
        }

        ConfigurationSection config = scoreboards.load();
        for (ConfigurationSection scoreboard : config.getChildren()) {
            if (scoreboard == null) continue;

            DisplayManager.addDisplay(new BoardDisplay(plugin, scoreboard));
        }
    }

    @Override
    public void tick() {
        title.tick();
        for (TextAnimation line : leftAligned) line.tick();
        for (TextAnimation line : rightAligned) line.tick();
    }
}
