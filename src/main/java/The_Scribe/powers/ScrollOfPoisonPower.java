package The_Scribe.powers;

import The_Scribe.cards.AbstractScribeCard;
import The_Scribe.cards.ScrollOfPoison;
import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.LoseEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

import java.util.Iterator;

//Gain 1 dex for the turn for each card played.

public class ScrollOfPoisonPower extends AbstractPower implements OnLoseBlockPower, OnLoseTempHpPower, CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("ScrollOfPoisonPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCROLL_OF_POISON_POWER);
    private static boolean isPlayerTurn = true;

    public ScrollOfPoisonPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        isPlayerTurn = true;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new ScrollOfPoisonPower(this.owner, this.amount);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public int onLoseTempHp(DamageInfo info, int damageAmount)
    {
        if (isPlayerTurn) {
            Iterator monsterIterator = AbstractDungeon.getMonsters().monsters.iterator();

            while (monsterIterator.hasNext()) {
                AbstractMonster m = (AbstractMonster) monsterIterator.next();
                if (!m.isDead && !m.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new PoisonPower(m, this.owner, (damageAmount * this.amount)), (damageAmount * this.amount)));
                }
            }
        }
        return damageAmount;
    }

    @Override
    public int onLoseBlock(DamageInfo info, int damageAmount)
    {
        if (isPlayerTurn) {
            Iterator monsterIterator = AbstractDungeon.getMonsters().monsters.iterator();

            while (monsterIterator.hasNext()) {
                AbstractMonster m = (AbstractMonster) monsterIterator.next();
                if (!m.isDead && !m.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new PoisonPower(m, this.owner, (damageAmount * this.amount)), (damageAmount * this.amount)));
                }
            }
        }
        return damageAmount;
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        if (isPlayerTurn) {
            Iterator monsterIterator = AbstractDungeon.getMonsters().monsters.iterator();

            while (monsterIterator.hasNext()) {
                AbstractMonster m = (AbstractMonster) monsterIterator.next();
                if (!m.isDead && !m.isDying) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this.owner, new PoisonPower(m, this.owner, (damageAmount * this.amount)), (damageAmount * this.amount)));
                }
            }
        }
        return damageAmount;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        isPlayerTurn = true;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer)
        {
            isPlayerTurn = false;
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        }
        if(this.amount > 1) {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }

    }

}
