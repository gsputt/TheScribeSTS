package The_Scribe.cards;

import The_Scribe.effects.LightningJumpEffect;
import The_Scribe.powers.CapacitanceScrollPower;
import The_Scribe.powers.SpellAttack;
import basemod.helpers.BaseModCardTags;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class LightningBlade extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("LightningBlade");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_LIGHTNING_BLADE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int DRAW_CARD = 0;
    private static final int UPGRADE_DRAW_CARD = 1;

    // /STAT DECLARATION/

    public LightningBlade() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = DRAW_CARD;
        this.magicNumber = this.baseMagicNumber;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.baseDamage = DAMAGE;
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if(AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount;
                this.calculateCardDamage(m);
            }
        }
        if(AbstractDungeon.player.hasPower(CapacitanceScrollPower.POWER_ID))
        {
           if(AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount > 0) {
               this.baseDamage += AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount;
               this.calculateCardDamage(m);
           }
        }
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AbstractGameAction.AttackEffect.NONE)));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningJumpEffect(p.hb.cX, p.hb.cY, m.drawX, m.drawY, false)));
        CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.1F);

        AbstractDungeon.actionManager
                .addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(m,
                        new DamageInfo(p, this.damage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.NONE));

        if(this.upgraded) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new LightningJumpEffect(m.drawX, m.drawY, CardGroup.DRAW_PILE_X, CardGroup.DRAW_PILE_Y, true)));
            AbstractDungeon.actionManager.addToBottom(
                    new DrawCardAction(p, this.magicNumber));
        }

    }

    @Override
    public void triggerWhenCopied() {
        this.baseDamage = DAMAGE;
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount;
            }
        }
        if(AbstractDungeon.player.hasPower(CapacitanceScrollPower.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount;
            }
        }
    }

    @Override
    public void triggerWhenDrawn() {
        this.baseDamage = DAMAGE;
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount;
            }
        }
        if(AbstractDungeon.player.hasPower(CapacitanceScrollPower.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount;
            }
        }
    }

    public void applyPowers() {
    super.applyPowers();
        this.baseDamage = DAMAGE;
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(SpellAttack.POWER_ID).amount;
            }
        }
        if(AbstractDungeon.player.hasPower(CapacitanceScrollPower.POWER_ID))
        {
            if(AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount > 0) {
                this.baseDamage += AbstractDungeon.player.getPower(CapacitanceScrollPower.POWER_ID).amount;
            }
        }
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new LightningBlade();
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_DRAW_CARD);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }
}