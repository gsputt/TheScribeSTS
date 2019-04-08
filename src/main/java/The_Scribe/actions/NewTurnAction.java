package The_Scribe.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static The_Scribe.ScribeMod.hasDisciple;

public class NewTurnAction extends AbstractGameAction {
    @Override
    public void update() {
        isDone = true;
        if(hasDisciple)
        {
            AbstractDungeon.actionManager.actions.remove(AbstractDungeon.actionManager.actions.size() - 1);
            AbstractDungeon.actionManager.actions.remove(AbstractDungeon.actionManager.actions.size() - 2);
            AbstractDungeon.actionManager.actions.remove(AbstractDungeon.actionManager.actions.size() - 3);
            AbstractDungeon.actionManager.endTurn();
            AbstractDungeon.actionManager.addToBottom(new DISCIPLE_Y_U_REPLACE_PATCH());
        }
        else {
            AbstractDungeon.actionManager.actions.remove(AbstractDungeon.actionManager.actions.size() - 1);
            AbstractDungeon.actionManager.endTurn();
        }
    }
}