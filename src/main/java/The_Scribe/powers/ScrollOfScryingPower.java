package The_Scribe.powers;

import The_Scribe.ScribeMod;
import The_Scribe.actions.ScrollOfScryingAction;
import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

//Gain 1 dex for the turn for each card played.

public class ScrollOfScryingPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = The_Scribe.ScribeMod.makeID("ScrollOfScryingPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCROLL_OF_SCRYING_POWER);
    private int cardsDrawn;

    public ScrollOfScryingPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        this.img = new Texture(IMG);
        this.canGoNegative = false;
        this.cardsDrawn = 190;
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new ScrollOfScryingPower(this.owner, this.amount);
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard)
    {
        if(cardsDrawn < this.amount && drawnCard.costForTurn > 0)
        {
            drawnCard.costForTurn = drawnCard.costForTurn - 1;
            drawnCard.isCostModifiedForTurn = true;
        }
        this.cardsDrawn++;
    }

    @Override
    public void atStartOfTurn() {
        this.cardsDrawn = 0;
        this.flashWithoutSound();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if(isPlayer)
        {
            this.flashWithoutSound();
            AbstractDungeon.actionManager.addToBottom(new ScrollOfScryingAction(AbstractDungeon.player, AbstractDungeon.player, this.amount, false));
        }
    }

    // Update the description when you apply this power. (i.e. add or remove an "s" in keyword(s))
    @Override
    public void updateDescription() {
        if(this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[1] + DESCRIPTIONS[3] + DESCRIPTIONS[1] + DESCRIPTIONS[4] + DESCRIPTIONS [5] + DESCRIPTIONS[7];
        }
        else
        {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[2] + DESCRIPTIONS[4] + DESCRIPTIONS[6] + DESCRIPTIONS[7];
        }
    }
}
