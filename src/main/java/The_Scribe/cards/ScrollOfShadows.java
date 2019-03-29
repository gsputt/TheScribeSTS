package The_Scribe.cards;

import The_Scribe.powers.Drained;
import The_Scribe.powers.ScrollOfShadowsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

import java.util.Iterator;

public class ScrollOfShadows extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("ScrollOfShadows");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_SCROLL_OF_SHADOWS);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 3;
    private static final int UPGRADE_COST = 2;
    private static int cardsPlayedThisTurn = 0;


    // /STAT DECLARATION/


    public ScrollOfShadows() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ScrollOfShadowsPower(p, 1, this.cardsPlayedThisTurn), 1));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = 0;
        Iterator cardsPlayedThisTurnIterator = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();

        while(cardsPlayedThisTurnIterator.hasNext()) {
            cardsPlayedThisTurnIterator.next();
            ++count;
        }
        cardsPlayedThisTurn = count;
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new ScrollOfShadows();
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