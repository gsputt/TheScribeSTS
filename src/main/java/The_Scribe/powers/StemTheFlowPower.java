package The_Scribe.powers;

import The_Scribe.effects.IcicleBlastEffect;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

//Gain 1 dex for the turn for each card played.

public class StemTheFlowPower extends AbstractPower implements OnReceivePowerPower, CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("StemTheFlowPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.STEM_THE_FLOW_POWER);
    public static int amountToCast = 0;
    private double DiminishingStacks = 1;
    private double DiminishAmount = 0.5;

    public StemTheFlowPower(AbstractCreature owner, int amount) {
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

    @Override
    public AbstractPower makeCopy()
    {
        return new StemTheFlowPower(this.owner, this.amount);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if(power.type == PowerType.DEBUFF && target == this.owner) {
            AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(this.owner, this.owner, Math.abs((int)Math.ceil(power.amount * DiminishingStacks))));
        }
        return true;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        amountToCast = this.amount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        DiminishingStacks = 1;
        if(this.amount > 1)
        {
            for(int i = 1; i <= this.amount - 1; i++)
            {
                DiminishingStacks += Math.pow(DiminishAmount, i);
            }
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1];
        }
        else {
            this.description = DESCRIPTIONS[0] + DiminishingStacks + DESCRIPTIONS[2];
        }

    }

}
