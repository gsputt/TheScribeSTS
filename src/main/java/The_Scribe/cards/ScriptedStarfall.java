package The_Scribe.cards;

import The_Scribe.patches.ScribeCardTags;
import The_Scribe.powers.Drained;
import The_Scribe.powers.ScriptedStarfallPower;
import The_Scribe.powers.SpellBlock;
import The_Scribe.powers.SpellSplit;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class ScriptedStarfall extends AbstractScribeCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("ScriptedStarfall");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_SCRIPTED_STARFALL);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.POWER;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int BASE_DAMAGE = 1;
    private static final int INCREASE = 1;
    private static final int UPGRADE_INCREASE = 1;
    private static final int DRAINED = 1;


    // /STAT DECLARATION/

    public ScriptedStarfall() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = INCREASE;
        this.magicNumber = this.baseMagicNumber;
        this.baseSecondMagicNumber = BASE_DAMAGE;
        this.secondMagicNumber = this.baseSecondMagicNumber;
        this.baseDrained = DRAINED;
        this.drained = this.baseDrained;
        AlwaysRetainField.alwaysRetain.set(this, true);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new Drained(p, this.drained), this.drained));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ScriptedStarfallPower(p, this.secondMagicNumber), this.secondMagicNumber));
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        this.secondMagicNumber += this.magicNumber;
        this.isSecondMagicNumberModified = true;
    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new ScriptedStarfall();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_INCREASE);
            this.initializeDescription();
        }
    }
}