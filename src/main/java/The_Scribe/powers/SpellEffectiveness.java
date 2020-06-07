package The_Scribe.powers;

import The_Scribe.ScribeMod;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

//Gain 1 dex for the turn for each card played.

public class SpellEffectiveness extends AbstractPower implements SpellModifierInterface, SpellsInterface, CloneablePowerInterface {

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

    @Override
    public AbstractPower makeCopy()
    {
        return new SpellEffectiveness(this.owner, this.amount);
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

        if (this.amount > 0) {
            this.description = DESCRIPTIONS[0] + FontHelper.colorString("" + (this.amount * 25) + "%", "b") + DESCRIPTIONS[1];
            this.type = PowerType.BUFF;
        } else {
            this.description = DESCRIPTIONS[0] + FontHelper.colorString("" + (this.amount * 25) + "%", "r") + DESCRIPTIONS[1];
            this.type = PowerType.DEBUFF;
        }


    }

}
