package The_Scribe.cards;

import The_Scribe.effects.ThrownScrollEffect;
import The_Scribe.powers.SpellsInterface;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
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
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static The_Scribe.patches.ScribeHoveredMonsterPatch.scribeHoveredMonster;

public class ThrownScroll extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("ThrownScroll");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_THROWN_SCROLL);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int AMOUNT = 3;
    private static final int UPGRADE_PLUS_AMOUNT = -1;

    private int totalPowerAmount = 0;

    // /STAT DECLARATION/

    public ThrownScroll() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = AMOUNT;
        this.magicNumber = this.baseMagicNumber;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        this.baseDamage = DAMAGE;
        this.totalPowerAmount = 0;
        for(AbstractPower power: AbstractDungeon.player.powers)
        {
            if(power instanceof SpellsInterface)
            {
                this.totalPowerAmount += power.amount;
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, power));
            }
        }
        AbstractScribeCard.removeScribedScrollPower();
        this.baseDamage += (int)Math.ceil((double)this.totalPowerAmount / (double)this.magicNumber);

        calculateCardDamage(m);

        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ThrownScrollEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY)));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.2F));
        AbstractDungeon.actionManager
                .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.SMASH));
    }

    @Override
    public void triggerWhenCopied() {
        this.baseDamage = DAMAGE;
        this.totalPowerAmount = 0;
        for(AbstractPower power: AbstractDungeon.player.powers)
        {
            if(power instanceof SpellsInterface)
            {
                this.totalPowerAmount += power.amount;
            }
        }
        this.baseDamage += (int)Math.ceil((double)this.totalPowerAmount / (double)this.magicNumber);
    }

    @Override
    public void triggerWhenDrawn() {
        this.baseDamage = DAMAGE;
        this.totalPowerAmount = 0;
        for(AbstractPower power: AbstractDungeon.player.powers)
        {
            if(power instanceof SpellsInterface)
            {
                this.totalPowerAmount += power.amount;
            }
        }
        this.baseDamage += (int)Math.ceil((double)this.totalPowerAmount / (double)this.magicNumber);
    }

    public void applyPowers() {
        super.applyPowers();
        this.baseDamage = DAMAGE;
        this.totalPowerAmount = 0;
        for(AbstractPower power: AbstractDungeon.player.powers)
        {
            if(power instanceof SpellsInterface)
            {
                this.totalPowerAmount += power.amount;
            }
        }
        this.baseDamage += (int)Math.ceil((double)this.totalPowerAmount / (double)this.magicNumber);
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new ThrownScroll();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_AMOUNT);
            this.initializeDescription();
        }
    }
}