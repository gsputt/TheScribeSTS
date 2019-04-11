package The_Scribe.relics;

import The_Scribe.cards.Cast_Spell;
import The_Scribe.patches.ScribeCardTags;
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
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import The_Scribe.ScribeMod;

public class PermafrostPen extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ScribeMod.makeID("PermafrostPen");
    public static final String IMG = ScribeMod.makePath(ScribeMod.PERMAFROST_PEN);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.PERMAFROST_PEN_OUTLINE);

    private static final int BLOCK_AMOUNT = 3;

    public PermafrostPen() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        if(c.hasTag(ScribeCardTags.SPELL_ATTACK) || c.hasTag(ScribeCardTags.SPELL_BLOCK)
                || c.hasTag(ScribeCardTags.SPELL_CLARITY) || c.hasTag(ScribeCardTags.SPELL_POISON)
                || c.hasTag(ScribeCardTags.SPELL_WEAK) || c.hasTag(ScribeCardTags.SPELL_SELF_DAMAGE)
                || c.hasTag(ScribeCardTags.SPELLSTONE_EFFECT) || c.hasTag(ScribeCardTags.SPELL_EFFECT_SCROLL))
        {
            AbstractDungeon.actionManager.addToBottom(
                    new com.megacrit.cardcrawl.actions.common.GainBlockAction(
                            AbstractDungeon.player, AbstractDungeon.player, BLOCK_AMOUNT));
            this.flash();
        }
    }


    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new PermafrostPen();
    }
}
