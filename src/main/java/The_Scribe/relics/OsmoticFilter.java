package The_Scribe.relics;

import The_Scribe.cards.Cast_Spell;
import The_Scribe.powers.Drained;
import The_Scribe.powers.EnergizedScribePower;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import The_Scribe.ScribeMod;

public class OsmoticFilter extends CustomRelic implements OnReceivePowerRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ScribeMod.makeID("OsmoticFilter");
    public static final String IMG = ScribeMod.makePath(ScribeMod.OSMOTIC_FILTER);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.OSMOTIC_FILTER_OUTLINE);

    public OsmoticFilter() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public boolean onReceivePower (AbstractPower power, AbstractCreature target) {
            if (power.ID == Drained.POWER_ID) {
                if(power.amount > 0) {
                    this.flash();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                            target, target, new EnergizedScribePower(target, power.amount), power.amount));
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
                }
            }
            return true;
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new OsmoticFilter();
    }
}
