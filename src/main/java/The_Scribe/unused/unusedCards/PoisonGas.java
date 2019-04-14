package The_Scribe.unused.unusedCards;

import The_Scribe.cards.AbstractScribeCard;
import The_Scribe.cards.CardSpellEffectInterface;
import The_Scribe.cards.CardSpellsInterface;
import The_Scribe.patches.ScribeCardTags;
import The_Scribe.powers.SpellPoison;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class PoisonGas extends AbstractScribeCard implements CardSpellEffectInterface, CardSpellsInterface {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("PoisonGas");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_POISON_GAS);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int POISON_AMOUNT = 4;
    private static final int UPGRADE_PLUS_POISON = 2;


    // /STAT DECLARATION/


    public PoisonGas() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = POISON_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        tags.add(ScribeCardTags.SPELL_POISON);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellPoison(p, this.magicNumber), this.magicNumber));
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new PoisonGas();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_POISON);
            this.initializeDescription();
        }
    }
}
