package The_Scribe.actions;

import The_Scribe.cards.AbstractScribeCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ScribedScrollAcquireCounterAction extends AbstractGameAction {
    @Override
    public void update() {
        isDone = true;
        AbstractScribeCard.ScribedScrollAcquireCounter -= 1;
    }
}
