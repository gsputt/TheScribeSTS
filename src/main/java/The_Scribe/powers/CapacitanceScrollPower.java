package The_Scribe.powers;

import The_Scribe.unused.unusedPowers.SpellVulnerable;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

//Gain 1 dex for the turn for each card played.

public class CapacitanceScrollPower extends AbstractPower {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("CapacitanceScrollPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.CAPACITANCE_SCROLL_POWER);

    public CapacitanceScrollPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atStartOfTurn() {
        boolean check = false;
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            check = true;
        }
        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            check = true;
        }
        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            check = true;
        }
        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
            check = true;
        }
        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            check = true;
        }
        if(AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
            check = true;
        }
        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
            check = true;
        }
        if(!check) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SpellAttack(this.owner, this.amount), this.amount));
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];

    }

}
