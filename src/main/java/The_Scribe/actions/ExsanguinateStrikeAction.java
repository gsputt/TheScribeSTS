package The_Scribe.actions;

import The_Scribe.effects.ExsanguinateEffect;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ExsanguinateStrikeAction extends AbstractGameAction {
    private DamageInfo damage;

    public ExsanguinateStrikeAction(AbstractCreature target, DamageInfo damage, AttackEffect effect) {
        this.damage = damage;
        this.setValues(target, damage);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
    }

    public void update() {
        if (this.duration == 0.5F) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }

        this.tickDuration();
        if (this.isDone) {
            this.gainTempHP(this.damage);
            this.target.damage(this.damage);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

    }

    private void gainTempHP(DamageInfo damage) {
        int tempHPAmount = damage.output;
        if (tempHPAmount >= 0) {
            tempHPAmount -= this.target.currentBlock;
            if (tempHPAmount > this.target.currentHealth) {
                tempHPAmount = this.target.currentHealth;
            }

            if (tempHPAmount > 0) {
                if (tempHPAmount > 1 && this.target.hasPower("Buffer")) {
                    return;
                }

                if (tempHPAmount > 1 && (this.target.hasPower("IntangiblePlayer") || this.target.hasPower("Intangible"))) {
                    tempHPAmount = 1;
                }

                tempHPAmount = tempHPAmount / 2;

                for(int i = 0; i < tempHPAmount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExsanguinateEffect(this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cX), 0F));
                }
                AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(this.source, this.source, tempHPAmount));

            }

        }
    }
}
