package The_Scribe.relics;

import The_Scribe.powers.*;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import The_Scribe.ScribeMod;

public class ClarityBossRelic extends CustomRelic{


    // ID, images, text.
    public static final String ID = ScribeMod.makeID("ClarityBossRelic");
    public static final String IMG = ScribeMod.makePath(ScribeMod.CLARITY_BOSS_RELIC);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.CLARITY_BOSS_RELIC_OUTLINE);

    public ClarityBossRelic() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void onPlayerEndTurn() {
        if(AbstractDungeon.player.hasPower(SpellAttack.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellAttack(
                            AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellBlock.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellBlock(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellBlock.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellChaining.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellChaining(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellChaining.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellClarity(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellClarity.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellEffectiveness(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellPoison.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellPoison(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellPoison.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellSelfDamage(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellSelfDamage.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellSplit.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellSplit(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(SpellWeak.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new SpellWeak(
                    AbstractDungeon.player, -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellWeak.POWER_ID).amount, true));
        }
        if(AbstractDungeon.player.hasPower(ScribedScrollAcquirePower.POWER_ID))
        {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, ScribedScrollAcquirePower.POWER_ID));
        }
    }

    @Override
    public void atTurnStartPostDraw() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player, new SpellClarity(
                AbstractDungeon.player, 1), 1, true));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new ClarityBossRelic();
    }
}
