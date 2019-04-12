package The_Scribe.cards;

import The_Scribe.effects.MagicMissilesEffect;
import The_Scribe.powers.*;
import The_Scribe.unused.unusedPowers.SpellVulnerable;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

import java.util.ArrayList;

public class MagicMissiles extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("MagicMissiles");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_MAGIC_MISSILES);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int DAMAGE = 3;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BASE_HIT_TIMES = 2;

    public static int hitTimes = 2;



    // /STAT DECLARATION/

    public MagicMissiles() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = hitTimes;
        this.magicNumber = this.baseMagicNumber;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        if (m != null) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new MagicMissilesEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.5F));
        }
        for(int i =  0; i < magicNumber; i++) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    public void applyPowers()
    {
        hitTimes = BASE_HIT_TIMES;

        for(AbstractPower power: AbstractDungeon.player.powers)
        {
            if(power instanceof SpellsInterface)
            {
                hitTimes += 1;
            }
        }
        /*
        if (AbstractDungeon.player.hasPower(SpellEffectiveness.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellSplit.POWER_ID)) {
            hitTimes += 1;
        }
        if(AbstractDungeon.player.hasPower(SpellClarity.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellAttack.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellBlock.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellPoison.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellVulnerable.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellWeak.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellSelfDamage.POWER_ID)) {
            hitTimes += 1;
        }
        if (AbstractDungeon.player.hasPower(SpellChaining.POWER_ID)) {
            hitTimes += 1;
        }*/
        this.baseMagicNumber = this.magicNumber = hitTimes;
        super.applyPowers();
    }


    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new MagicMissiles();
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