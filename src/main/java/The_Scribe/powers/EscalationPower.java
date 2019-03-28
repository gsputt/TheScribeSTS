package The_Scribe.powers;

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
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

//Gain 1 dex for the turn for each card played.

public class EscalationPower extends AbstractPower implements OnReceivePowerPower {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("EscalationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.ESCALATION_POWER);
    public static int amountToCast = 0;
    private boolean doubleNextPowerUse;

    public EscalationPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        amountToCast = this.amount;
        doubleNextPowerUse = true;
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if(target == this.owner && source == this.owner)
        {
            if(power.amount > 0)
            {
                if(this.doubleNextPowerUse) {
                    if (power.ID.equals(SpellAttack.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellAttack(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                    if (power.ID.equals(SpellBlock.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellBlock(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                    if (power.ID.equals(SpellClarity.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellClarity(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                    if (power.ID.equals(SpellPoison.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellPoison(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                    if (power.ID.equals(SpellVulnerable.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellVulnerable(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                    if (power.ID.equals(SpellWeak.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellWeak(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                    if (power.ID.equals(SpellSelfDamage.POWER_ID)) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, source, new SpellSelfDamage(source, power.amount), power.amount));
                        doubleNextPowerUse = false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void atStartOfTurn() {
        this.doubleNextPowerUse = true;
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];

    }

}
