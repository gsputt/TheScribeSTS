package The_Scribe.cards;

import The_Scribe.patches.ScribeCardTags;
import The_Scribe.powers.Drained;
import The_Scribe.powers.EscalationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
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

public class Escalation extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("Escalation");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_ESCALATION);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 2;
    private static final int DRAINED_AMOUNT = 1;
    private static int cardsPlayedThisTurn = 0;


    // /STAT DECLARATION/


    public Escalation() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = DRAINED_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        this.isInnate = false;

    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new EscalationPower(p, 1, this.cardsPlayedThisTurn), 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new Drained(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        int count = 0;

        Iterator cardsPlayedThisTurnIterator = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();

        while(cardsPlayedThisTurnIterator.hasNext()) {
            AbstractCard c = (AbstractCard)cardsPlayedThisTurnIterator.next();
            if (c.hasTag(ScribeCardTags.SPELL_ATTACK) || c.hasTag(ScribeCardTags.SPELL_BLOCK)
                    || c.hasTag(ScribeCardTags.SPELL_CLARITY) || c.hasTag(ScribeCardTags.SPELL_POISON)
                    || c.hasTag(ScribeCardTags.SPELL_WEAK) || c.hasTag(ScribeCardTags.SPELL_SELF_DAMAGE)
                    || c.hasTag(ScribeCardTags.SPELLSTONE_EFFECT) || c.hasTag(ScribeCardTags.SPELL_EFFECT_SCROLL)) {
                ++count;
            }
        }
        cardsPlayedThisTurn = count;
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new Escalation();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}