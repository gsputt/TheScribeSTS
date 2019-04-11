package The_Scribe.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import java.util.Iterator;

public class ScrollOfScryingAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    public static int numPlaced;

    public ScrollOfScryingAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this.target = target;
        this.p = (AbstractPlayer)target;
        this.setValues(target, source, amount);
        this.actionType = ActionType.CARD_MANIPULATION;
        this.isRandom = isRandom;
    }

    public void update() {
        if (this.duration == 0.5F) {
            if (this.p.hand.size() < this.amount) {
                this.amount = this.p.hand.size();
            }

            int i;
            if (this.isRandom) {
                for(i = 0; i < this.amount; ++i) {
                    this.p.hand.moveToDeck(this.p.hand.getRandomCard(false), false);
                }
            } else {
                    numPlaced = this.amount;
                    AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, true, false, false, true);
                    this.tickDuration();
                    return;
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var3 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while(var3.hasNext()) {
                AbstractCard c = (AbstractCard)var3.next();
                this.p.hand.moveToDeck(c, false);
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("PutOnDeckAction");
        TEXT = uiStrings.TEXT;
    }
}
