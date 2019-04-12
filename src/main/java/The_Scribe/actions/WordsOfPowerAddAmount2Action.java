//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package The_Scribe.actions;

import The_Scribe.powers.WordsOfPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class WordsOfPowerAddAmount2Action extends AbstractGameAction {
    private int amount2Apply;

    public WordsOfPowerAddAmount2Action(AbstractCreature target, int amount2Apply) {
        this.target = target;
        this.amount2Apply = amount2Apply;
        this.actionType = ActionType.POWER;
        this.duration = 0.01F;
    }

    public void update() {
        if(target.hasPower(WordsOfPowerPower.POWER_ID)) {
            if (target.getPower(WordsOfPowerPower.POWER_ID) instanceof TwoAmountPower) {
                ((TwoAmountPower)target.getPower(WordsOfPowerPower.POWER_ID)).amount2 += this.amount2Apply;
                target.getPower(WordsOfPowerPower.POWER_ID).updateDescription();
            }
        }
        this.isDone = true;
    }
}
