package The_Scribe.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ScribeSelfDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private int goldAmount;
    private static final float DURATION = 0.1F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private boolean skipWait;
    private boolean muteSfx;

    public ScribeSelfDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this.goldAmount = 0;
        this.skipWait = false;
        this.muteSfx = false;
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
    }


    public ScribeSelfDamageAction(AbstractCreature target, DamageInfo info) {
        this(target, info, AttackEffect.NONE);
    }

    public ScribeSelfDamageAction(AbstractCreature target, DamageInfo info, boolean superFast) {
        this(target, info, AttackEffect.NONE);
        this.skipWait = superFast;
    }

    public ScribeSelfDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast) {
        this(target, info, effect);
        this.skipWait = superFast;
    }

    public ScribeSelfDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, boolean superFast, boolean muteSfx) {
        this(target, info, effect, superFast);
        this.muteSfx = muteSfx;
    }

    public void update() {
        if (this.shouldCancelAction() && this.info.type != DamageType.THORNS) {
            this.isDone = true;
        } else {
            if (this.duration == 0.1F) {
                if (this.info.type != DamageType.THORNS && (this.info.owner.isDying || this.info.owner.halfDead)) {
                    this.isDone = true;
                    return;
                }

                //this.target.damageFlash = true;
                //this.target.damageFlashFrames = 4;
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect, this.muteSfx));
            }

            this.tickDuration();
            if (this.isDone) {
                if (this.attackEffect == AttackEffect.POISON) {
                    this.target.tint.color = Color.CHARTREUSE.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                } else if (this.attackEffect == AttackEffect.FIRE) {
                    this.target.tint.color = Color.RED.cpy();
                    this.target.tint.changeColor(Color.WHITE.cpy());
                }

                if(!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    this.target.damage(this.info);
                }
                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                }

                if (!this.skipWait && !Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
                }
            }

        }
    }
}
