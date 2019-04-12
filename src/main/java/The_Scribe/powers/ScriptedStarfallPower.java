package The_Scribe.powers;

import The_Scribe.effects.ScriptedStarfallEffect;
import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;

import java.util.Iterator;

//Gain 1 dex for the turn for each card played.

public class ScriptedStarfallPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("ScriptedStarfallPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIPTED_STARFALL_POWER);
    public static int amountToCast = 0;

    public ScriptedStarfallPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        amountToCast = this.amount;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new ScriptedStarfallPower(this.owner, this.amount);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        amountToCast = this.amount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer)
        {
            AbstractMonster monster;
            Iterator monsterIterator = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while(monsterIterator.hasNext())
            {
                monster = (AbstractMonster)monsterIterator.next();
                if(monster.currentHealth > 0 && !monster.isDeadOrEscaped() && !monster.halfDead) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ScriptedStarfallEffect(monster.hb.cX, monster.hb.cY)));
                }
            }

            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));

            monsterIterator = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            while(monsterIterator.hasNext())
            {
                monster = (AbstractMonster)monsterIterator.next();
                if(monster.currentHealth > 0 && !monster.isDeadOrEscaped() && !monster.halfDead) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(monster, new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
                }
            }
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

}
