package The_Scribe.cards;

import The_Scribe.powers.SpellEffectiveness;
import The_Scribe.powers.SpellSplit;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

public class EnchantedStrike extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("EnchantedStrike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_ENCHANTED_STRIKE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_PLUS_DMG = 4;

    // /STAT DECLARATION/

    public EnchantedStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = 0;
        this.magicNumber = this.baseMagicNumber;
        tags.add(AbstractCard.CardTags.STRIKE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int i = this.magicNumber;
        while(i > 0) {
            AbstractDungeon.actionManager
                    .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                            new DamageInfo(p, this.damage, this.damageTypeForTurn),
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            i--;
        }

        AbstractDungeon.actionManager
                .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    public void applyPowers() {
        super.applyPowers();
        this.baseDamage = DAMAGE;
        if(this.upgraded) {
            this.baseDamage = DAMAGE + UPGRADE_PLUS_DMG;
        }
        if (AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            this.baseDamage = (int)(this.baseDamage*(1 + (AbstractDungeon.player.getPower(SpellEffectiveness.POWER_ID).amount * 0.01 * 25)));
        }
        if(AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
            this.baseMagicNumber = AbstractDungeon.player.getPower(SpellSplit.POWER_ID).amount;
            this.magicNumber = this.baseMagicNumber;
        }
        else
        {
            this.baseMagicNumber = 0;
            this.magicNumber = this.baseMagicNumber;
        }
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new EnchantedStrike();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.initializeDescription();
        }
    }
}