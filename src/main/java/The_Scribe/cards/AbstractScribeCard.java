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
        this.baseSecondMagicNumber += amount;
        this.secondMagicNumber = this.baseSecondMagicNumber;
        this.upgradeSecondMagicNumber = true;
    }

    public void upgradeAnomalousTextsVariable(int amount)
    {
        this.baseAnomalousTextsVariable += amount;
        this.anomalousTextsVariable = this.baseAnomalousTextsVariable;
        this.upgradeAnomalousTextsVariable = true;
    }

    public void upgradeSpellBlock(int amount)
    {
        this.baseSpellBlock += amount;
        this.spellBlock = this.baseSpellBlock;
        this.upgradeSpellBlock = true;
    }

    public void upgradeSelfDamage(int amount)
    {
        this.baseSelfDamage += amount;
        this.selfDamage = this.baseSelfDamage;
        this.upgradeSelfDamage = true;
    }

    public void upgradeSpellDamage(int amount)
    {
        this.baseSpellDamage += amount;
        this.spellDamage = this.baseSpellDamage;
        this.upgradeSpellDamage = true;
    }

    public void upgradeDrained(int amount)
    {
        this.baseDrained += amount;
        this.drained = this.baseDrained;
        this.upgradeDrained = true;
    }

    public void upgradeSplit(int amount)
    {
        this.baseSplit += amount;
        this.split = this.baseSplit;
        this.upgradeSplit = true;
    }


    public void upgradeEffectivity(int amount)
    {
        this.baseEffectivity += amount;
        this.effectivity = this.baseEffectivity;
        this.upgradeEffectivity = true;
    }

}