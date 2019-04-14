package The_Scribe.cards;

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

public class Conflagrate extends CustomCard {

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to defaultmod/DefaultMod.java, Line ~140 (Image path section).
     *
     * Strike Deal 7(9) damage.
     */

    // TEXT DECLARATION

    public static final String ID = ScribeMod.makeID("Conflagrate");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    // Yes, you totally can use "defaultModResources/images/cards/Attack.png" instead and that would work.
    // It might be easier to use that while testing.
    // Using makePath is good practice once you get the hand of things, as it prevents you from
    // having to change *every single card/file/path* if the image path changes due to updates or your personal preference.

    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_CONFLAGRATE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 3;
    private static final int DAMAGE = 35;
    private static final int UPGRADE_PLUS_DMG = 15;
    private static final int POST_DAMAGE_MULTIPLIER = 2;

    // /STAT DECLARATION/

    public Conflagrate() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractMonster targetMonster;
        int remainingDamage = this.damage;
        int i = 0;
        int targetBeforeHealthAndBlock;
        while(i < AbstractDungeon.getCurrRoom().monsters.monsters.size())
        {
            targetMonster = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
            if(!(targetMonster.isDead || targetMonster.currentHealth <= 0 || targetMonster.halfDead)) {
                targetBeforeHealthAndBlock = targetMonster.currentBlock + targetMonster.currentHealth;
                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.DamageAction(targetMonster,
                        new DamageInfo(p, remainingDamage, this.damageTypeForTurn),
                        AbstractGameAction.AttackEffect.FIRE));
                remainingDamage = (remainingDamage - targetBeforeHealthAndBlock) * POST_DAMAGE_MULTIPLIER;
            }
            if(remainingDamage <= 0 || targetMonster.hasPower("IntangiblePlayer") || targetMonster.hasPower("Buffer") || targetMonster.hasPower("Intangible")) {
                break;
            }
            i++;
        }
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new Conflagrate();
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