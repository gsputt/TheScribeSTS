package The_Scribe.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExhaustTopCardOfDrawPileAction extends AbstractGameAction {

    public ExhaustTopCardOfDrawPileAction() {
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
        AbstractDungeon.player.drawPile.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);
        AbstractDungeon.player.limbo.group.add(card);
        AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
        AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));

        this.isDone = true;
    }
}
