package better.scoreboard.core.animation.impl;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.animation.Animation;
import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.Nullable;
import sharkbyte.configuration.core.ConfigSection;

public class ColorAnimation extends Animation<BossBar.Color> {

    public ColorAnimation(BetterScoreboard plugin, @Nullable ConfigSection config) {
        super(plugin, config);

        if (config == null) {
            animation.add(BossBar.Color.PURPLE);
            return;
        }

        for (String color : config.getList(String.class, "color")) animation.add(BossBar.Color.NAMES.value(color.toLowerCase()));
        if (random) currentIndex = (int) (animation.size() * Math.random());
    }
}
