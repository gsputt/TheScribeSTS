package The_Scribe.cards;

import The_Scribe.powers.Drained;
import The_Scribe.powers.ScrollOfScryingPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;


public class ScrollOfScrying extends AbstractScribeCard {

    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("ScrollOfScrying");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_SCROLL_OF_SCRYING);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int DRAINED_AMOUNT = 3;
    private static final int UPGRADE_DRAINED = -1;

    // /STAT DECLARATION/


    public ScrollOfScrying() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDrained = DRAINED_AMOUNT;
        this.drained = this.baseDrained;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new Drained(p, this.drained), this.drained));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ScrollOfScryingPower(p, 1), 1));
    }



    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new ScrollOfScrying();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDrained(UPGRADE_DRAINED);
            this.initializeDescription();
        }
    }
}