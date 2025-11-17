package better.scoreboard.core.animation.impl;

import better.scoreboard.core.BetterScoreboard;
import better.scoreboard.core.animation.Animation;
import better.scoreboard.core.configuration.ConfigurationSection;
import net.kyori.adventure.bossbar.BossBar;
import org.jetbrains.annotations.Nullable;

public class DivisionAnimation  extends Animation<BossBar.Overlay> {

    public DivisionAnimation(BetterScoreboard plugin, @Nullable ConfigurationSection config) {
        super(plugin, config);

        if (config == null) {
            animation.add(BossBar.Overlay.PROGRESS);
            return;
        }

        for (int division : config.getList(Integer.class, "division")) {
            switch (division) {
                case 20:
                    animation.add(BossBar.Overlay.NOTCHED_20);
                    break;
                case 12:
                    animation.add(BossBar.Overlay.NOTCHED_12);
                    break;
                case 10:
                    animation.add(BossBar.Overlay.NOTCHED_10);
                    break;
                case 6:
                    animation.add(BossBar.Overlay.NOTCHED_6);
                    break;
                default:
                    animation.add(BossBar.Overlay.PROGRESS);
                    break;
            }
        }

        if (random) currentIndex = (int) (animation.size() * Math.random());
    }
}
