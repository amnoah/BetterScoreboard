package better.scoreboard.core.animation.impl;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.animation.Animation;
import better.scoreboard.core.configuration.ConfigurationSection;
import org.jetbrains.annotations.Nullable;

public class HealthAnimation extends Animation<Double> {

    public HealthAnimation(BetterScoreboard plugin, @Nullable ConfigurationSection config) {
        super(plugin, config);

        if (config == null) {
            animation.add(1.0);
            return;
        }

        animation.addAll(config.getList(Double.class, "health"));
        if (random) currentIndex = (int) (animation.size() * Math.random());
    }
}
