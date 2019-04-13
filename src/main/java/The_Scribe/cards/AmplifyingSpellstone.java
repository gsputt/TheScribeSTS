package The_Scribe.cards;

import The_Scribe.powers.SpellEffectiveness;
import com.evacipated.cardcrawl.mod.stslib.variables.ExhaustiveVariable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class AmplifyingSpellstone extends AbstractScribeCard implements CardSpellModifierInterface, CardSpellsInterface {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("AmplifyingSpellstone");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_AMPLIFYING_SPELLSTONE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    public static final int EFFECTIVITY = 5;
    public static final int UPGRADE_PLUS_EFFECTIVITY = 3;

    // /STAT DECLARATION/

    public AmplifyingSpellstone() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseEffectivity = EFFECTIVITY;
        this.effectivity = this.baseEffectivity;
        ExhaustiveVariable.setBaseValue(this, 2);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    public void triggerOnExhaust() {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player,
                        AbstractDungeon.player,
                        new SpellEffectiveness(AbstractDungeon.player,
                                this.effectivity), this.effectivity));
    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new AmplifyingSpellstone();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeEffectivity(UPGRADE_PLUS_EFFECTIVITY);
            this.initializeDescription();
        }
    }
}