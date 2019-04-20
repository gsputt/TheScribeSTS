package The_Scribe.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseTempHpPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.Iterator;


//Gain 1 dex for the turn for each card played.

public class BloodMagicksPower extends AbstractPower implements OnLoseBlockPower, OnLoseTempHpPower, CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("BloodMagicksPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.BLOOD_MAGICKS_POWER);
    private static boolean isPlayerTurn = true;

    public BloodMagicksPower(AbstractCreature owner, int amount) {
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

    @Override
    public int onLoseTempHp(DamageInfo info, int damageAmount)
    {
        if (isPlayerTurn) {
            BloodMagicksStuff();
        }
        return damageAmount;
    }

    @Override
    public int onLoseBlock(DamageInfo info, int damageAmount)
    {
        if (isPlayerTurn) {
            BloodMagicksStuff();
        }
        return damageAmount;
    }

    @Override
    public int onLoseHp(int damageAmount)
    {
        if (isPlayerTurn) {
            BloodMagicksStuff();
        }
        return damageAmount;
    }

    public void BloodMagicksStuff()
    {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, this.amount));
        AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.amount));
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

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }


    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        }
        else
        {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        }

    }

}
