package The_Scribe.cards;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.abstracts.CustomCard;

import The_Scribe.ScribeMod;
import The_Scribe.patches.AbstractCardEnum;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class QuickStudy extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = The_Scribe.ScribeMod.makeID("QuickStudy");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = ScribeMod.makePath(ScribeMod.SCRIBE_QUICK_STUDY);

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = AbstractCardEnum.SCRIBE_BLUE;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;


    // /STAT DECLARATION/


    public QuickStudy() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

        AbstractCard c = returnTrulyRandomCardFromMasterDeckListInCombat(AbstractDungeon.miscRng);
        if(c != null) {
            c = c.makeSameInstanceOf();
            c.setCostForTurn(0);
            if(c.exhaust == false) {
                c.exhaust = true;
                c.rawDescription += " NL Exhaust.";
            }
            if(c.isEthereal == false)
            {
                c.isEthereal = true;
                c.rawDescription += " NL Ethereal.";
            }
            if(!c.name.contains("Echo: ")) {
                c.name = "Echo: " + c.name;
            }
            c.initializeDescription();
            if(AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE)
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(c));
            }
            else
            {
                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(c));
            }
        }
    }

    private static AbstractCard returnTrulyRandomCardFromMasterDeckListInCombat(Random rng) {
        ArrayList<AbstractCard> list = new ArrayList<>();
        Iterator masterDeckIterator = AbstractDungeon.player.masterDeck.group.iterator();

        while(masterDeckIterator.hasNext()) {
            AbstractCard c = (AbstractCard)masterDeckIterator.next();
            if(c.cost >= -1 && c.type != CardType.CURSE) {
                list.add(c);
            }
        }

        if(list.size() <= 0)
        {
            return null;
        }
        else {
            return (AbstractCard) list.get(rng.random(list.size() - 1));
        }
    }

    // Which card to return when making a copy of this card.
    @Override
    public AbstractCard makeCopy() {
        return new QuickStudy();
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