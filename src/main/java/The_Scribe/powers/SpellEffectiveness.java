package The_Scribe.powers;

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

public class SpellEffectiveness extends AbstractPower {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("SpellEffectiveness");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SPELL_FOCUSED);
    public static int amountToCast = 0;

    public SpellEffectiveness(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = true;
        amountToCast = this.amount;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        amountToCast = this.amount;
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        /*if(this.amount > 0) {
            this.type = PowerType.BUFF;
        }
        else if(this.amount < 0) {
            this.type = PowerType.DEBUFF;
        }*/
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount * 25 + DESCRIPTIONS[1];

        if (this.amount > 0) {
            this.type = PowerType.BUFF;
        } else {
            this.type = PowerType.DEBUFF;
        }


    }

}
