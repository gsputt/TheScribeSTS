package The_Scribe.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import The_Scribe.actions.WordsOfPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

//Gain 1 dex for the turn for each card played.

public class WordsOfPowerPower extends TwoAmountPower implements CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("WordsOfPowerPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.WORDS_OF_POWER_POWER);

    public WordsOfPowerPower(AbstractCreature owner, int amount, int amount2) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        this.amount2 += amount2;
    }


    @Override
    public AbstractPower makeCopy()
    {
        return new WordsOfPowerPower(this.owner, this.amount, this.amount2);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if(this.amount2 >= 1) {
            if(power.type == PowerType.BUFF) {
                if(!(power instanceof InvisiblePower)) {
                    dealDamage();
                }
            }
        }
        else {
            if(power.type == PowerType.BUFF) {
                if(power.amount < 0) {
                    if(!(power instanceof InvisiblePower)) {
                        dealDamage();
                    }
                }
            }
        }
    }

    private void dealDamage()
    {
        AbstractDungeon.actionManager.addToBottom(
                new WordsOfPowerAction(AbstractDungeon.getMonsters().getRandomMonster((AbstractMonster)null, true, AbstractDungeon.cardRandomRng),
                        new DamageInfo(AbstractDungeon.player, this.amount, DamageInfo.DamageType.THORNS), 1));
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount2 >= 1) {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
        else
        {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2];
        }


    }

}
