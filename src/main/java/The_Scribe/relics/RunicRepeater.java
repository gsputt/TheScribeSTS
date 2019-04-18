package The_Scribe.relics;

import The_Scribe.actions.RunicRepeaterAction;
import The_Scribe.powers.SpellAttack;
import The_Scribe.powers.SpellModifierInterface;
import The_Scribe.powers.SpellPoison;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import The_Scribe.ScribeMod;

public class RunicRepeater extends CustomRelic implements OnReceivePowerRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ScribeMod.makeID("RunicRepeater");
    public static final String IMG = ScribeMod.makePath(ScribeMod.RUNIC_REPEATER);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.RUNIC_REPEATER_OUTLINE);


    public RunicRepeater() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.RARE, LandingSound.FLAT);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature source) {
        if(power instanceof SpellModifierInterface)
        {
            AbstractDungeon.actionManager.addToBottom(new RunicRepeaterAction());
            this.flash();
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
        return new RunicRepeater();
    }
}
