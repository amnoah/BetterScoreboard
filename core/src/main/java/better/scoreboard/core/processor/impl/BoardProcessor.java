package better.scoreboard.core.processor.impl;

import better.scoreboard.core.display.impl.BoardDisplay;
import better.scoreboard.core.processor.Processor;
import com.github.retrooper.packetevents.protocol.player.User;
import org.jetbrains.annotations.Nullable;
import sharkbyte.scoreboard.core.SBScoreboard;

public class BoardProcessor extends Processor<BoardDisplay> {

    private SBScoreboard scoreboard;

    public BoardProcessor(User user) {
        super(BoardDisplay.class, user);
    }

    @Override
    public void switchDisplay(@Nullable BoardDisplay display) {
        if (display == null) {
            if (super.display == null) return;
            super.display = null;
            scoreboard.destroy();
            return;
        }

        scoreboard = SBScoreboard.createScoreboard(user, "bsimpltest", display.getTitle().getAnimation().getText(user));
        scoreboard.create();
        scoreboard.display();

        super.display = display;
        // Set active lines.
        for (int i = 0; i < display.getLineCount(); i++) {
            if (display.getLeftText(i).isConditionalTrue(user))
            scoreboard.setLeftAlignedText(i,
                    super.display.getLeftText(i).isConditionalTrue(user) ?
                            display.getLeftText(i).getAnimation().getText(user) :
                            null
            );
            scoreboard.setRightAlignedText(i,
                    super.display.getRightText(i).isConditionalTrue(user) ?
                            display.getRightText(i).getAnimation().getText(user) :
                            null
            );
        }
        // Remove unused lines.
        for (int i = display.getLineCount(); i < 15; i++) {
            scoreboard.setLeftAlignedText(i, null);
            scoreboard.setRightAlignedText(i, null);
        }

        scoreboard.update();
    }

    @Override
    public void tick() {
        if (display == null) return;

        if (display.getTitle().isUpdateTick()) {
            scoreboard.setTitle(display.getTitle().getAnimation().getText(user));
        }
        for (int i = 0; i < display.getLineCount(); i++) {
            if (display.getLeftText(i).isUpdateTick()) {
                if (!display.getLeftText(i).isConditionalTrue(user)) {
                    scoreboard.setLeftAlignedText(i, null);
                } else {
                    scoreboard.setLeftAlignedText(i, display.getLeftText(i).getAnimation().getText(user));
                }
            }

            if (display.getRightText(i).isUpdateTick()) {
                if (!display.getRightText(i).isConditionalTrue(user))
                    scoreboard.setRightAlignedText(i, null);
                else
                    scoreboard.setRightAlignedText(i, display.getRightText(i).getAnimation().getText(user));
            }
        }

        scoreboard.update();
    }
}
