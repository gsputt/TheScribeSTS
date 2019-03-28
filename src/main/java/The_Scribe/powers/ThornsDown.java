package The_Scribe.powers;

import The_Scribe.effects.IcicleBlastEffect;
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

public class ThornsDown extends AbstractPower {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("ThornsDown");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.THORNS_DOWN);
    public static int amountToCast = 0;

    public ThornsDown(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        amountToCast = this.amount;
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        amountToCast = this.amount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        if(AbstractDungeon.player.hasPower(ThornsPower.POWER_ID)) {
            if(AbstractDungeon.player.getPower(ThornsPower.POWER_ID).amount <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ThornsPower.POWER_ID));
            }
        }
    }


    public int onAttacked(DamageInfo info, int damageAmount) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new IcicleBlastEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.0F));
        return damageAmount;
    }

    public void atStartOfTurnPostDraw() {
        if(AbstractDungeon.player.hasPower(ThornsPower.POWER_ID) && AbstractDungeon.player.hasPower(ThornsDown.POWER_ID)) {
            if(AbstractDungeon.player.getPower(ThornsDown.POWER_ID).amount >= AbstractDungeon.player.getPower(ThornsPower.POWER_ID).amount)
            {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, ThornsPower.POWER_ID));
            }
            else
            {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player,
                        AbstractDungeon.player, new ThornsPower(AbstractDungeon.player,
                        -AbstractDungeon.player.getPower(ThornsDown.POWER_ID).amount),
                        -AbstractDungeon.player.getPower(ThornsDown.POWER_ID).amount));
            }
        }

        if(AbstractDungeon.player.hasPower(ThornsDown.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];

    }

}
