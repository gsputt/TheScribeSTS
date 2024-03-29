package The_Scribe.cards;

import The_Scribe.patches.ScribeCardTags;
import The_Scribe.powers.SpellSelfDamage;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import The_Scribe.powers.SpellAttack;

public class SanguineSurge extends AbstractScribeCard implements CardSpellEffectInterface, CardSpellsInterface {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("SanguineSurge");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_SANGUINE_SURGE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int SELF_DAMAGE = 3;
    private static final int UPGRADE_SELF_DAMAGE = -1;

    // /STAT DECLARATION/

    public SanguineSurge() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = DAMAGE;
        this.magicNumber = this.baseMagicNumber;
        this.baseSelfDamage = SELF_DAMAGE;
        this.selfDamage = this.baseSelfDamage;
        tags.add(ScribeCardTags.SPELL_ATTACK);
        tags.add(ScribeCardTags.SPELL_SELF_DAMAGE);
        this.purgeOnUse = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = this.makeCopy();
        if(this.upgraded)
        {
            card.upgrade();
        }
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, 1));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellAttack(p, this.magicNumber), this.magicNumber));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSelfDamage(p, this.selfDamage), this.selfDamage));
    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new SanguineSurge();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_DMG);
            this.upgradeSelfDamage(UPGRADE_SELF_DAMAGE);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}