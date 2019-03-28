package The_Scribe.cards;

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

public class AnomalousTexts extends AbstractScribeCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("AnomalousTexts");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_ANOMALOUS_TEXTS);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int INVERSION_EFFECTIVITY_NUMBER = -2;
    private static final int UPGRADE_INVERSION_EFFECTIVITY_NUMBER = 1;
    private static final int CONVERSION_EFFECTIVITY = 4;
    private static final int UPGRADE_CONVERSION_EFFECTIVITY = 2;
    private static int effectivityToAdd = 0;

    // /STAT DECLARATION/

    public AnomalousTexts() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseEffectivity = INVERSION_EFFECTIVITY_NUMBER;
        this.effectivity = this.baseEffectivity;
        this.baseAnomalousTextsVariable = CONVERSION_EFFECTIVITY;
        this.anomalousTextsVariable = this.baseAnomalousTextsVariable;
        this.exhaust = true;
        effectivityToAdd = 0;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        effectivityToAdd = 0;
        if(AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount < 0) {
                effectivityToAdd = (int)((double)(AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount) / (this.effectivity));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p,
                        -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount),
                        -AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount));
                if(effectivityToAdd > 0) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p,
                            effectivityToAdd), effectivityToAdd));
                }
            }
        }

        if (AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellEffectiveness(p,
                    (AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount) * this.anomalousTextsVariable),
                    (AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount) * this.anomalousTextsVariable));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSplit(p,
                    -AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount),
                    -AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount));
        }

    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new AnomalousTexts();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeEffectivity(UPGRADE_INVERSION_EFFECTIVITY_NUMBER);
            this.upgradeAnomalousTextsVariable(UPGRADE_CONVERSION_EFFECTIVITY);
            this.initializeDescription();
        }
    }
}