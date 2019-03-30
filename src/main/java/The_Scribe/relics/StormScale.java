package The_Scribe.relics;

import The_Scribe.powers.SpellAttack;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import The_Scribe.ScribeMod;

public class StormScale extends CustomRelic implements OnReceivePowerRelic, OnAfterUseCardRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ScribeMod.makeID("StormScale");
    public static final String IMG = ScribeMod.makePath(ScribeMod.STORM_SCALE);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.STORM_SCALE_OUTLINE);

    public StormScale() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature source) {
        return true;
    }

    @Override
    public int onReceivePowerStacks(AbstractPower power, AbstractCreature source, int stackAmount) {
        if (power.owner == source) {
            if (power.ID.equals(SpellAttack.POWER_ID)) {
                if(power.amount > 0 && stackAmount > 0) {
                    power.amount += this.counter;
                    flash();
                    return stackAmount + this.counter;
                }
            }
        }
            return stackAmount;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.counter++;
    }

    @Override
    public void atTurnStart() {
        counter = 0;
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];

    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new StormScale();
    }
}
