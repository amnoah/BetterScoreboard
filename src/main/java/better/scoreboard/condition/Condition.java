package better.scoreboard.condition;

import better.scoreboard.util.Line;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Condition {

    private enum Mode {
        AND,
        OR
    }

    private final List<Criteria> criteria = new ArrayList<>();
    private final Line falseLine, trueLine;
    private final Mode mode;

    public Condition(ConfigurationSection section) {
        for (String string : section.getStringList("criteria")) criteria.add(new Criteria(string));
        switch (section.getString("mode", "and").toLowerCase()) {
            case "or":
                this.mode = Mode.OR;
                break;
            case "and":
            default:
                this.mode = Mode.AND;
                break;
        }

        falseLine = new Line(section.getString("false"));
        trueLine = new Line(section.getString("true"));
    }

    public String getText(Player player) {
        if (mode.equals(Mode.AND)) {
            for (Criteria criteria : this.criteria) if (!criteria.canRun(player)) return falseLine.getText(player);
            return trueLine.getText(player);
        } else {
            for (Criteria criteria : this.criteria) if (criteria.canRun(player)) return trueLine.getText(player);
            return falseLine.getText(player);
        }
    }
}
