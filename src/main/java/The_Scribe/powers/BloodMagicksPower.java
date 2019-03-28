package The_Scribe.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;


//Gain 1 dex for the turn for each card played.

public class BloodMagicksPower extends AbstractPower {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("BloodMagicksPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.BLOOD_MAGICKS_POWER);
    public static int amountToCast = 0;
    public static final int HPLOSS_MULTIPLIER = 2;
    public static final int HPLOSS_MULTIPLIER_UPGRADED = 1;
    public static int energyCostRetain = 0;

    public BloodMagicksPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        amountToCast = this.amount;
    }

    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(this.amount <= 1 && this.amount > 0) {
            if(card.freeToPlayOnce) {
                //doNothing
            }
            else if(card.costForTurn < 0) {
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, EnergyPanel.totalCount * HPLOSS_MULTIPLIER, AbstractGameAction.AttackEffect.NONE));
            }
            else if(card.costForTurn > 0){
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, card.costForTurn * HPLOSS_MULTIPLIER, AbstractGameAction.AttackEffect.NONE));
            }
        }
        else
        {
            if(card.freeToPlayOnce) {
                //doNothing
            }
            else if(card.costForTurn < 0) {
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, EnergyPanel.totalCount * HPLOSS_MULTIPLIER_UPGRADED, AbstractGameAction.AttackEffect.NONE));
            }
            else if(card.costForTurn > 0){
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this.owner, this.owner, card.costForTurn * HPLOSS_MULTIPLIER_UPGRADED, AbstractGameAction.AttackEffect.NONE));
            }
        }
        if(card.costForTurn > 0) {
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(card.costForTurn));
        }
        if(card.costForTurn < 0) {
            energyCostRetain = EnergyPanel.getCurrentEnergy();
        }
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if(card.costForTurn < 0) {
            AbstractDungeon.actionManager.addToTop(new GainEnergyAction(energyCostRetain));
        }
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        amountToCast = this.amount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount > 1) {
            this.description = DESCRIPTIONS[1];
        }
        else
        {
            this.description = DESCRIPTIONS[0];
        }

    }

}
