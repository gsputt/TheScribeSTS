package The_Scribe.cards;

import The_Scribe.actions.FranticSearchExhaustAction;
import basemod.BaseMod;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class FranticSearch extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("FranticSearch");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_FRANTIC_SEARCH);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 0;
    private static final int DRAW = 5;
    private static final int UPGRADE_PLUS_DRAW = 1;


    // /STAT DECLARATION/


    public FranticSearch() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = DRAW;
        this.magicNumber = this.baseMagicNumber;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ExpertiseAction(p, BaseMod.MAX_HAND_SIZE));
        AbstractDungeon.actionManager.addToBottom(new FranticSearchExhaustAction());
        AbstractDungeon.actionManager.addToBottom(new ExpertiseAction(p, this.magicNumber));
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new FranticSearch();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DRAW);
            this.initializeDescription();
        }
    }
}