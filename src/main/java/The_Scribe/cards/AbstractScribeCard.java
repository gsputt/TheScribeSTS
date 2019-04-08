package The_Scribe.cards;

import The_Scribe.actions.ScribedScrollAcquireCounterAction;
import The_Scribe.powers.*;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;


public abstract class AbstractScribeCard extends CustomCard {

    public static int ScribedScrollAcquireCounter = 0;

    public AbstractScribeCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color,
                                  CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type,
                color, rarity, target);
    }

    public static void ScribedScrollAcquire()
    {
        if(!(ScribedScrollAcquireCounter >= 1))
        {
            ScribedScrollAcquireCounter += 1;
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new ScribedScrollAcquirePower(
                            AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new ScribedScrollAcquireCounterAction());
        }
    }


    public int effectivity;
    public int baseEffectivity;
    public boolean isEffectivityModified;
    public boolean upgradeEffectivity;

    public int split;
    public int baseSplit;
    public boolean isSplitModified;
    public boolean upgradeSplit;

    public int drained;
    public int baseDrained;
    public boolean isDrainedModified;
    public boolean upgradeDrained;

    public int spellDamage;
    public int baseSpellDamage;
    public boolean isSpellDamageModified;
    public boolean upgradeSpellDamage;

    public int selfDamage;
    public int baseSelfDamage;
    public boolean isSelfDamageModified;
    public boolean upgradeSelfDamage;

    public int spellBlock;
    public int baseSpellBlock;
    public boolean isSpellBlockModified;
    public boolean upgradeSpellBlock;

    public int anomalousTextsVariable;
    public int baseAnomalousTextsVariable;
    public boolean isAnomalousTextsVariableModified;
    public boolean upgradeAnomalousTextsVariable;

    public int secondMagicNumber;
    public int baseSecondMagicNumber;
    public boolean isSecondMagicNumberModified;
    public boolean upgradeSecondMagicNumber;

    public void upgradeSecondMagicNumber(int amount)
    {
        this.secondMagicNumber += amount;
        if(this.secondMagicNumber > this.baseSecondMagicNumber || amount > 0)
        {
            this.isSecondMagicNumberModified = true;
        }
    }

    public void upgradeAnomalousTextsVariable(int amount)
    {
        this.anomalousTextsVariable += amount;
        if(this.anomalousTextsVariable > this.baseAnomalousTextsVariable || amount > 0)
        {
            this.isAnomalousTextsVariableModified = true;
        }
    }

    public void upgradeSpellBlock(int amount)
    {
        this.spellBlock += amount;
        if(this.spellBlock > this.spellBlock || amount > 0)
        {
            this.isSpellBlockModified = true;
        }
    }

    public void upgradeSelfDamage(int amount)
    {
        this.selfDamage += amount;
        if(this.selfDamage > this.baseSelfDamage || amount > 0)
        {
            this.isSelfDamageModified = true;
        }
    }

    public void upgradeSpellDamage(int amount)
    {
        this.spellDamage += amount;
        if (this.spellDamage > this.baseSpellDamage || amount > 0)
        {
            this.isSpellDamageModified = true;
        }
    }

    public void upgradeDrained(int amount)
    {
        this.drained += amount;
        if (this.drained > this.baseDrained || amount > 0)
        {
            this.isDrainedModified = true;
        }
    }

    public void upgradeSplit(int amount)
    {
        this.split += amount;
        if (this.split > this.baseSplit || amount > 0)
        {
            this.isSplitModified = true;
        }
    }


    public void upgradeEffectivity(int amount)
    {
        this.effectivity += amount;

        /*if (AbstractDungeon.player != null) {
            if (AbstractDungeon.player.drawPile.contains(this) || AbstractDungeon.player.hand.contains(this) || AbstractDungeon.player.discardPile.contains(this) || AbstractDungeon.player.exhaustPile.contains(this)){
                //do something
            }

        }*/
        if (this.effectivity > this.baseEffectivity || amount > 0)
        {
            this.isEffectivityModified = true;
        }
    }

}