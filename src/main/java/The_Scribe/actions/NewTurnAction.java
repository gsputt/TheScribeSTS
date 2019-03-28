package The_Scribe.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class NewTurnAction extends AbstractGameAction {
    @Override
    public void update() {
        isDone = true;
        AbstractDungeon.actionManager.actions.remove(AbstractDungeon.actionManager.actions.size() - 1);
        AbstractDungeon.actionManager.endTurn();
    }
}