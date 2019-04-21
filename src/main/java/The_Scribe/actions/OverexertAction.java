package The_Scribe.actions;

import The_Scribe.powers.Drained;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Iterator;

public class OverexertAction extends AbstractGameAction {
    //private boolean exhaustCards;
    public static final String TEXT = "to play twice and exhaust";
    private boolean isRandom;

    public OverexertAction(AbstractCreature target, int amount, boolean isRandom) {
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.WAIT;
        this.source = AbstractDungeon.player;
        this.target = target;
        this.amount = amount;
        this.isRandom = !isRandom;
        //this.exhaustCards = exhausts;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player.hand.size() == 0) {
                this.isDone = true;
                return;
            }
            if (AbstractDungeon.player.hand.size() != 0) {
                int i;
                if (this.isRandom) {
                    for (i = 0; i < this.amount; ++i) {
                        //do Stuff
                        AbstractCard cardToDoStuffOn = AbstractDungeon.player.hand.getRandomCard(true);
                        gainDrained(cardToDoStuffOn);
                        doStuff(cardToDoStuffOn);
                    }
                } else {
                    AbstractDungeon.handCardSelectScreen.open(TEXT, this.amount, false, false, false, false, false);
                    this.tickDuration();
                    return;
                }
            }
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            Iterator var3 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator();

            while (var3.hasNext()) {
                AbstractCard card = (AbstractCard) var3.next();
                gainDrained(card);
                doStuff(card);

            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }
        this.tickDuration();
    }

    private void gainDrained(AbstractCard card)
    {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Drained(AbstractDungeon.player, card.costForTurn), card.costForTurn));
    }

    private void doStuff(AbstractCard card) {
        AbstractDungeon.player.hand.group.remove(card);
        AbstractDungeon.getCurrRoom().souls.remove(card);
        card.freeToPlayOnce = true;
        card.exhaustOnUseOnce = true;
        AbstractDungeon.player.limbo.addToBottom(card);
        card.current_y = -200.0F * Settings.scale;
        card.target_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
        card.target_y = (float) Settings.HEIGHT / 2.0F;
        card.targetAngle = 0.0F;
        card.lighten(false);
        card.drawScale = 0.12F;
        card.targetDrawScale = 0.75F;


        AbstractCard tmp = card.makeSameInstanceOf();
        AbstractDungeon.player.limbo.addToBottom(tmp);
        //tmp.current_x = card.current_x;
        tmp.current_y = card.current_y;
        tmp.target_x = (float)Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
        tmp.target_y = (float)Settings.HEIGHT / 2.0F;
        tmp.freeToPlayOnce = true;
        /*AbstractMonster m = null;
        if (this.target instanceof AbstractMonster) {
            m = (AbstractMonster)this.target;
        }*/
        tmp.purgeOnUse = true;

        if (!card.canUse(AbstractDungeon.player, (AbstractMonster) this.target)) {
            AbstractDungeon.actionManager.addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
        } else {
            card.applyPowers();

            AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
            AbstractDungeon.actionManager.addToTop(new UnlimboAction(tmp));

            AbstractDungeon.actionManager.addToTop(new QueueCardAction(tmp, this.target));
            AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, this.target));
            //AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse));



            if (!Settings.FAST_MODE) {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
            } else {
                AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
            }
        }
    }


}



