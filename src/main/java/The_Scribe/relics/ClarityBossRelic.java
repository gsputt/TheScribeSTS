package The_Scribe.relics;

import The_Scribe.powers.*;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
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
        for(AbstractPower p: AbstractDungeon.player.powers)
        {
            if(p instanceof SpellsInterface)
            {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, p));
            }
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
