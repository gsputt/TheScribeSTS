package The_Scribe.cards;

import The_Scribe.effects.AbsoluteZeroSnowflakeEffect;
import The_Scribe.powers.AbsoluteZeroPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;

import java.util.Iterator;

public class AbsoluteZero extends CustomCard {

    // TEXT DECLARATION
    public static final String ID = The_Scribe.ScribeMod.makeID("AbsoluteZero");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_ABSOLUTE_ZERO);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    // /TEXT DECLARATION/


    // STAT DECLARATION
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 3;
    private static final int GIVE_BLOCK_AMOUNT = 50;
    private static final int GIVE_BLOCK_UPGRADE_AMOUNT = -20;
    // /STAT DECLARATION/

    public AbsoluteZero() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = GIVE_BLOCK_AMOUNT;
        this.magicNumber = this.baseMagicNumber;
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Iterator monsterIterator = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();
        AbstractMonster targetMonster;
        while(monsterIterator.hasNext()) {
            targetMonster = (AbstractMonster) monsterIterator.next();
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(targetMonster, p, this.magicNumber));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new AbsoluteZeroSnowflakeEffect(p.hb.cX, p.hb.cY, targetMonster.hb.cX, targetMonster.hb.cY), 0F));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new AbsoluteZeroPower(p, 1), 1));
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new AbsoluteZero();
    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(GIVE_BLOCK_UPGRADE_AMOUNT);
            this.initializeDescription();
        }
    }
}
