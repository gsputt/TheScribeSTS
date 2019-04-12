package The_Scribe.powers;

import basemod.BaseMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

//Gain 1 dex for the turn for each card played.

public class MaelstromPower extends TwoAmountPower implements CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("MaelstromPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.MAELSTROM_POWER);

    public MaelstromPower(AbstractCreature owner, int amount, int amount2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        this.amount2 = amount2;
        this.canGoNegative2 = false;
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new MaelstromPower(this.owner, this.amount, this.amount2);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(this.amount2 < this.amount)
        {
            if(AbstractDungeon.player.hasPower(Drained.POWER_ID))
            {
                AbstractCard c = returnTrulyRandomCardFromMasterDeckListInCombat(AbstractDungeon.miscRng, card.costForTurn);
                if(c != null) {
                    c = c.makeStatEquivalentCopy();
                    c.setCostForTurn(0);
                    if(c.exhaust == false) {
                        c.exhaust = true;
                        c.rawDescription += " NL Exhaust.";
                    }
                    if(c.isEthereal == false)
                    {
                        c.isEthereal = true;
                        c.rawDescription += " NL Ethereal.";
                    }
                    if(!c.name.contains("Echo: ")) {
                        c.name = "Echo: " + c.name;
                    }
                    c.initializeDescription();
                    if(AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE)
                    {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
                    }
                    else
                    {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
                    }

                    this.flashWithoutSound();
                }
            }
        }
        this.amount2++;
        updateDescription();
    }


    @Override
    public void atStartOfTurn() {
        this.amount2 = 0;
    }


    private static AbstractCard returnTrulyRandomCardFromMasterDeckListInCombat(Random rng, int cardCost) {
        ArrayList<AbstractCard> list = new ArrayList();
        Iterator masterDeckIterator = AbstractDungeon.player.masterDeck.group.iterator();

        while(masterDeckIterator.hasNext()) {
            AbstractCard c = (AbstractCard)masterDeckIterator.next();
            if(c.cost <= cardCost && c.cost >= -1) {
                list.add(c);
            }
        }

        if(list.size() <= 0)
        {
            return null;
        }
        else {
            return (AbstractCard) list.get(rng.random(list.size() - 1));
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        }
        if(this.amount > 1) {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[1];
        }

        if(this.amount2 == 1)
        {
            this.description += DESCRIPTIONS[4] + this.amount2 + DESCRIPTIONS[5];
        }
        if(this.amount2 > 1)
        {
            this.description += DESCRIPTIONS[4] + this.amount2 + DESCRIPTIONS[6];
        }

    }

}
