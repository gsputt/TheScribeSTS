package The_Scribe.powers;

import The_Scribe.cards.Cast_Spell;
import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

import The_Scribe.ScribeMod;

import java.util.Iterator;

//Gain 1 dex for the turn for each card played.

public class ScribedScrollAcquirePower extends AbstractPower implements InvisiblePower {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("ScribedScrollAcquirePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBED_SCROLL_ACQUIRE_POWER);



    public ScribedScrollAcquirePower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;

        if(!checkForScribedSpell())
        {
            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new Cast_Spell(), 1));
            }
        }

    }

    @Override
    public void atStartOfTurn() {
        if(!checkForScribedSpell())
        {
            if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
                AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(new Cast_Spell(), 1));
            }
        }
    }

    private boolean checkForScribedSpell() {
        Iterator handIterator = AbstractDungeon.player.hand.group.iterator();
        while(handIterator.hasNext()) {
            AbstractCard c = (AbstractCard)handIterator.next();
            if(c.cardID.equals(The_Scribe.ScribeMod.makeID("Cast_Spell")))
            {
                return true;
            }
        }
        return false;
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

}
