package The_Scribe.actions;


import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.ActionType;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.ArrayList;
import java.util.Iterator;

public class DivinationAction extends AbstractGameAction {
    private static final String ID = ScribeMod.makeID("DivinationAction");
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);
    public static final String TEXT = UI_STRINGS.TEXT[0];
    private float startingDuration;
    private CardGroup tmpGroup;

    public DivinationAction(int numCards) {
        this.amount = numCards;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.startingDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startingDuration;
        CardGroup tmpGroup = new CardGroup(CardGroupType.UNSPECIFIED);
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            if (AbstractDungeon.player.drawPile.isEmpty()) {
                this.isDone = true;
                return;
            }

            tmpGroup = new CardGroup(CardGroupType.UNSPECIFIED);

            for(int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
                tmpGroup.addToTop((AbstractCard)AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
            }

            AbstractDungeon.gridSelectScreen.open(tmpGroup, 1, false, TEXT);
        } else if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            Iterator var3 = AbstractDungeon.gridSelectScreen.selectedCards.iterator();
            AbstractCard c = null;
            while(var3.hasNext()) {
                c = (AbstractCard)var3.next();
                AbstractDungeon.player.drawPile.moveToHand(c, AbstractDungeon.player.drawPile);
            }
            if(!tmpGroup.isEmpty())
            {
                ArrayList<AbstractCard> exhaustIterator = tmpGroup.group;
                for(AbstractCard card : exhaustIterator)
                {
                    if(c != null) {
                        if (!card.uuid.equals(c.uuid))
                        {
                            AbstractDungeon.player.drawPile.moveToExhaustPile(card);
                        }
                    }
                }
            }


            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }

        this.tickDuration();
    }
}
