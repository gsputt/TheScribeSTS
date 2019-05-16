package The_Scribe.relics;

import The_Scribe.cards.Cast_Spell;
import The_Scribe.powers.Drained;
import The_Scribe.powers.EnergizedScribePower;
import The_Scribe.powers.RemoveSplitAtEndOfTurnPower;
import The_Scribe.powers.SpellEffectiveness;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
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

public class MeditationCircle extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ScribeMod.makeID("MeditationCircle");
    public static final String IMG = ScribeMod.makePath(ScribeMod.MEDITATION_CIRCLE);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.MEDITATION_CIRCLE_OUTLINE);

    public MeditationCircle() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.BOSS, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellEffectiveness(AbstractDungeon.player, 1), 1));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            public void update() {
                if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID))
                {
                    if(AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount <= 2)
                    {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SpellEffectiveness(AbstractDungeon.player, 1), 1));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new RemoveSplitAtEndOfTurnPower(AbstractDungeon.player, 1), 1));
                    }
                }
                this.isDone = true;
            }
        });

    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new MeditationCircle();
    }
}
