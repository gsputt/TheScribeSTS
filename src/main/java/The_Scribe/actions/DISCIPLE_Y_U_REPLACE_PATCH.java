package The_Scribe.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DISCIPLE_Y_U_REPLACE_PATCH extends AbstractGameAction {
    @Override
    public void update()
    {
        isDone = true;
        AbstractDungeon.actionManager.monsterAttacksQueued = true;
    }
}
