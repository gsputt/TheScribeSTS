package The_Scribe.cards;

import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class BarrierOfLight extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("BarrierOfLight");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_BARRIER_OF_LIGHT);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 4;
    private static final int BLOCK_PER_HEALTH = 3;
    private static final int UPGRADE_COST = 3;
    private boolean costHasBeenModified;
    private int costBeforeModify;
    private boolean recalculateCost = false;


    // /STAT DECLARATION/


    public BarrierOfLight() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = 0;
        this.baseMagicNumber = BLOCK_PER_HEALTH;
        this.magicNumber = this.baseMagicNumber;
        this.costHasBeenModified = false;
        this.recalculateCost = false;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new com.megacrit.cardcrawl.actions.common.GainBlockAction(p, p, this.block));
    }

    @Override
    public void applyPowers()
    {
        if(!this.costHasBeenModified) {
            this.costBeforeModify = this.costForTurn;
        }
        super.applyPowers();
        this.baseBlock = (int)Math.ceil(((double)AbstractDungeon.player.maxHealth) /(double)magicNumber);
        double currentHPPercent = ((double)AbstractDungeon.player.currentHealth / (double)AbstractDungeon.player.maxHealth);
        if(!this.costHasBeenModified || this.recalculateCost) {
            this.costForTurn = this.costBeforeModify;
            if (currentHPPercent <= 0.25) {
                this.modifyCostForTurn(-1);
            }
            if (currentHPPercent <= 0.50) {
                this.modifyCostForTurn(-1);
            }
            if (currentHPPercent <= 0.75) {
                this.modifyCostForTurn(-1);
            }
            this.costHasBeenModified = true;
            this.recalculateCost = false;
        }
    }

    @Override
    public void tookDamage()
    {
        this.recalculateCost = true;
    }

    @Override
    public void triggerWhenDrawn()
    {
        this.costHasBeenModified = false;
        this.baseBlock = (int)Math.ceil(((double)AbstractDungeon.player.maxHealth) /(double)magicNumber);
    }

    @Override
    public void triggerWhenCopied()
    {
        this.costHasBeenModified = false;
        this.baseBlock = (int)Math.ceil(((double)AbstractDungeon.player.maxHealth) /(double)magicNumber);
    }

    /*
    @Override
    public void tookDamage() {
        this.baseBlock = (int)((double)(AbstractDungeon.player.maxHealth) / magicNumber);
        double currentHPPercent = ((double)AbstractDungeon.player.currentHealth / AbstractDungeon.player.maxHealth);
        if(currentHPPercent <= 0.25) {
            this.setCostForTurn(this.costForTurn - 1);
        }
        if(currentHPPercent <= 0.5) {
            this.setCostForTurn(this.costForTurn - 1);
        }
        if(currentHPPercent <= 0.75) {
            this.setCostForTurn(this.costForTurn - 1);
        }
    }
    @Override
    public void triggerWhenDrawn() {
        this.baseBlock = (int)((double)(AbstractDungeon.player.maxHealth) / magicNumber);
        double currentHPPercent = ((double)AbstractDungeon.player.currentHealth / AbstractDungeon.player.maxHealth);
        if(currentHPPercent <= 0.25) {
            this.setCostForTurn(this.costForTurn - 1);
        }
        if(currentHPPercent <= 0.5) {
            this.setCostForTurn(this.costForTurn - 1);
        }
        if(currentHPPercent <= 0.75) {
            this.setCostForTurn(this.costForTurn - 1);
        }
    }
    @Override
    public void triggerWhenCopied() {
        this.baseBlock = (int)((double)(AbstractDungeon.player.maxHealth) / magicNumber);
        double currentHPPercent = ((double)AbstractDungeon.player.currentHealth / AbstractDungeon.player.maxHealth);
        if(currentHPPercent <= 0.25) {
            this.setCostForTurn(this.costForTurn - 1);
        }
        if(currentHPPercent <= 0.5) {
            this.setCostForTurn(this.costForTurn - 1);
        }
        if(currentHPPercent <= 0.75) {
            this.setCostForTurn(this.costForTurn - 1);
        }
    }
*/
    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new BarrierOfLight();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADE_COST);
            this.initializeDescription();
        }
    }
}