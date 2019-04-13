package The_Scribe.powers;

import The_Scribe.cards.AbstractScribeCard;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

//Gain 1 dex for the turn for each card played.

public class SpellPiercingBolts extends AbstractPower implements SpellModifierInterface, SpellsInterface, CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("SpellPiercingBolts");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SPELL_PIERCING_BOLTS);
    private double DiminishingStacks = 2;
    private double DiminishAmount = 0.5;

    public SpellPiercingBolts(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;

        DiminishingStacks = 2;
        if(this.amount > 1)
        {
            for(int i = 1; i <= this.amount - 1; i++)
            {
                DiminishingStacks += Math.pow(DiminishAmount, i);
            }
        }
        updateDescription();
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new SpellPiercingBolts(this.owner, this.amount);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        DiminishingStacks = 2;
        if(this.amount > 1)
        {
            for(int i = 1; i <= this.amount - 1; i++)
            {
                DiminishingStacks += Math.pow(DiminishAmount, i);
            }
        }
        updateDescription();
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + DiminishingStacks + DESCRIPTIONS[1];
    }

}
