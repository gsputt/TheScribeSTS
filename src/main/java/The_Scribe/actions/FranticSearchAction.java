package The_Scribe.actions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class FranticSearchAction extends AbstractGameAction {

    public FranticSearchAction(AbstractCreature source, int amount) {
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.WAIT;
    }

    public void update() {
        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
            this.isDone = true;
            return;
        }
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            AbstractDungeon.actionManager.addToTop(new FranticSearchAction(this.source, this.amount));
            AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
            this.isDone = true;
            return;
        }
        if(this.amount > AbstractDungeon.player.drawPile.size() && !AbstractDungeon.player.discardPile.isEmpty())
        {
            AbstractDungeon.actionManager.addToTop(new FranticSearchAction(this.source, this.amount));
            AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
            this.isDone = true;
            return;
        }

        int toDraw = (BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size());
        int remainingToExhaust = this.amount - toDraw;

        if (remainingToExhaust > 0) {
            for (int i = 0; i < remainingToExhaust; i++) {
                AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                    public void update() {
                        if (!AbstractDungeon.player.drawPile.isEmpty()) {
                            AbstractDungeon.actionManager.addToTop(new ExhaustTopCardOfDrawPileAction());
                        }
                        this.isDone = true;

                    }
                });
            }
        }

        if (toDraw > 0) {
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(this.source, toDraw));
        }
        this.isDone = true;
    }
}
