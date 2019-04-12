package The_Scribe.cards;

import The_Scribe.patches.ScribeCardTags;
import The_Scribe.powers.SpellPoison;
import The_Scribe.powers.SpellSelfDamage;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import java.util.Iterator;

public class RecklessResponse extends AbstractScribeCard implements SpellEffectInterface, SpellsInterface{

    /*
     * Wiki-page: https://github.com/daviscook477/BaseMod/wiki/Custom-Cards
     *
     * In order to understand how image paths work, go to The_Scribe/ScribeMod.java, Line ~140 (Image path section).
     *
     * Defend Gain 5 (8) block.
     */


    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("RecklessResponse");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_RECKLESS_RESPONSE);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 0;
    private static final int VULNERABLE_AMOUNT = 2;
    private static final int SELF_DAMAGE = 4;


    // /STAT DECLARATION/


    public RecklessResponse() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseSelfDamage = SELF_DAMAGE;
        this.selfDamage = this.baseSelfDamage;
        this.baseMagicNumber = VULNERABLE_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        tags.add(ScribeCardTags.SPELL_SELF_DAMAGE);
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded)
        {
            Iterator monstersToDebuff = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
            AbstractMonster monsterApplyDebuff;
            while(monstersToDebuff.hasNext()) {
                monsterApplyDebuff = (AbstractMonster) monstersToDebuff.next();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(monsterApplyDebuff, p, new VulnerablePower(monsterApplyDebuff, this.magicNumber, false), this.magicNumber));
            }
        }
        else
        {
            int i = 0;
            AbstractMonster targetMonster;
            while(i < AbstractDungeon.getCurrRoom().monsters.monsters.size()) {
                targetMonster = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
                if(!(targetMonster.isDead || targetMonster.currentHealth <= 0 || targetMonster.halfDead)) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(targetMonster, p, new VulnerablePower(targetMonster, this.magicNumber, false), this.magicNumber));
                    break;
                }
                else
                {
                    i++;
                }
            }
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new SpellSelfDamage(p, this.baseSelfDamage), this.baseSelfDamage));
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new RecklessResponse();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.target = CardTarget.ALL;
            this.initializeDescription();
        }
    }
}
