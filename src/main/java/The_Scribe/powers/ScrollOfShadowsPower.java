package The_Scribe.powers;

import The_Scribe.patches.ScribeCardTags;
import basemod.BaseMod;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

//Gain 1 dex for the turn for each card played.

public class ScrollOfShadowsPower extends TwoAmountPower implements CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("ScrollOfShadowsPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCROLL_OF_SHADOWS_POWER);

    public ScrollOfShadowsPower(AbstractCreature owner, int amount, int amount2) {
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
        return new ScrollOfShadowsPower(this.owner, this.amount, this.amount2);
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
        if (this.amount2 < this.amount) {
            AbstractCard c = card;
            if (c.type == AbstractCard.CardType.POWER) {
                if (c != null) {
                    c = c.makeSameInstanceOf();
                    c.modifyCostForCombat(-1);
                    ScribeMod.addEchoDescription(c);
                    if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                        AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
                    } else {
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


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if (Settings.language == Settings.GameLanguage.RUS) {
            rusDescription();
        }
        else
        {
            engDescription();
        }

    }

    private void engDescription()
    {
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

    private void rusDescription()
    {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[4] + this.amount2;
        }
        if (this.amount > 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4] + this.amount2;
        }
    }

}
