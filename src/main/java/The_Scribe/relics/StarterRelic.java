package The_Scribe.relics;

import The_Scribe.cards.Cast_Spell;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.CodexAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import basemod.abstracts.CustomRelic;
import The_Scribe.ScribeMod;

public class StarterRelic extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ScribeMod.makeID("StarterRelic1");
    public static final String IMG = ScribeMod.makePath(ScribeMod.STARTER_RELIC_1);
    public static final String OUTLINE = ScribeMod.makePath(ScribeMod.STARTER_RELIC_OUTLINE_1);

    public StarterRelic() {
        super(ID, new Texture(IMG), new Texture(OUTLINE), RelicTier.STARTER, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new CodexAction());
    }

    /*
    @Override
    public void atTurnStart() {
        //AbstractDungeon.actionManager.addToTop(new GainEnergyAction(1));
        if(this.counter == 0) {
            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new Cast_Spell(), 1));
            }
            flash();
        }
        this.counter++;
        while(this.counter >= 2)
        {
            this.counter -= 2;
        }
    }*/

    // Flash at the start of Battle.
    /*@Override
    public void atBattleStartPreDraw() {
        flash();
    }*/

    // Gain 1 energy on equip.
    /*@Override
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    // Lose 1 energy on unequip.
    @Override
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster -= 1;
    }*/

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Which relic to return on making a copy of this relic.
    @Override
    public AbstractRelic makeCopy() {
        return new StarterRelic();
    }
}
