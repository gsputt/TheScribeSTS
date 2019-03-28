package The_Scribe.unused.unusedCards;

import The_Scribe.cards.AbstractScribeCard;
import The_Scribe.powers.SpellEffectiveness;
import The_Scribe.powers.SpellSplit;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class Conversion extends AbstractScribeCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("Conversion");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_CONVERSION);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public static final int EFFECTIVITY = 4;

    // /STAT DECLARATION/


    public Conversion() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        this.baseEffectivity = EFFECTIVITY;
        this.effectivity = baseEffectivity;
        upgradeEffectivity(0);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p,
                    (AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount) * EFFECTIVITY),
                    (AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount) * EFFECTIVITY));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSplit(p,
                    -AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount));
        }
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new Conversion();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(UPGRADED_COST);
            this.initializeDescription();
        }
    }
}

